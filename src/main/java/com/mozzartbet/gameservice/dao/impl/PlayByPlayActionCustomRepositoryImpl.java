package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mozzartbet.gameservice.dao.PlayByPlayActionCustomRepository;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.dto.TeamQuarterPoints;

public class PlayByPlayActionCustomRepositoryImpl implements PlayByPlayActionCustomRepository {
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<TeamQuarterPoints> getQuarterPoints(Long matchId) {
		
		MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(Criteria.where("matchId").is(matchId),
				Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Point")));

		GroupOperation groupOperation = Aggregation.group("quarter", "team")
				.sum("points").as("quarterPoints")
				.last("matchId").as("matchId");
		
		ProjectionOperation projectToMatchModel = Aggregation.project()
				.andExpression("quarter").as("quarter")
				.andExpression("quarterPoints").as("points")
				.andExpression("matchId").as("matchId");
		
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectToMatchModel);

		AggregationResults<TeamQuarterPoints> result = mongoTemplate.aggregate(aggregation, "play_by_play_actions",
				TeamQuarterPoints.class);

		return result.getMappedResults();
	}

	@Override
	public List<PlayerStats> getPlayerStats(Long matchId) {
		
		// FG
		MatchOperation matchOperation1 = Aggregation.match(new Criteria().andOperator(Criteria.where("matchId").is(matchId),
				Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Point"),
				Criteria.where("points").gte(2)));

		GroupOperation groupOperation1 = Aggregation.group("pointPlayer").count().as("FG").first("matchId") .as("matchId").first("pointPlayer").as("player");

		ProjectionOperation projectToMatchModel1 = Aggregation.project().andExpression("player").as("player")
				.andExpression("FG").as("fieldGoals").andExpression("matchId").as("matchId").andExclude("_id");

		Aggregation aggregation1 = Aggregation.newAggregation(matchOperation1, groupOperation1, projectToMatchModel1);

		AggregationResults<PlayerStats> result1 = mongoTemplate.aggregate(aggregation1, "play_by_play_actions", PlayerStats.class);

		List<PlayerStats> playersStats = result1.getMappedResults();

		// FGA
		MatchOperation matchOperation2 = Aggregation.match(new Criteria()
				.andOperator(Criteria.where("matchId").is(matchId), Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Miss"))
				.orOperator(Criteria.where("description").regex(".*misses 2-pt.*"), Criteria.where("description").regex(".*misses 3-pt.*")));

		GroupOperation groupOperation2 = Aggregation.group("missPlayer").count().as("misses").first("missPlayer").as("player").first("matchId").as("matchId");

		ProjectionOperation projectToMatchModel2 = Aggregation.project().andExpression("player").as("player")
				.andExpression("misses").as("misses").andExpression("matchId").as("matchId");

		Aggregation aggregation2 = Aggregation.newAggregation(matchOperation2, groupOperation2, projectToMatchModel2);

		AggregationResults<Document> result2 = mongoTemplate.aggregate(aggregation2, "play_by_play_actions", Document.class);

		List<Document> resultList2 = result2.getMappedResults();
		
		// 3 PT
		MatchOperation threePointMatch = Aggregation.match(new Criteria().andOperator(Criteria.where("matchId").is(1L),
				Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Point"),
				Criteria.where("points").gt(2)));

		GroupOperation threePointGroup = Aggregation.group("pointPlayer").count().as("threePoints").first("matchId")
				.as("matchId").first("pointPlayer").as("player");

		ProjectionOperation threePointProjection = Aggregation.project().andExpression("player").as("player")
				.andExpression("threePoints").as("threePointFieldGoals").andExpression("matchId").as("matchId").andExclude("_id");

		Aggregation threePointAggregation = Aggregation.newAggregation(threePointMatch, threePointGroup, threePointProjection);

		AggregationResults<Document> threePointAggregationResult = mongoTemplate.aggregate(threePointAggregation, "play_by_play_actions", Document.class);

		List<Document> threePointPlayersStats = threePointAggregationResult.getMappedResults();
		
		// 3 PTA
		MatchOperation threePTAMatch = Aggregation.match(new Criteria()
				.andOperator(Criteria.where("matchId").is(matchId), Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Miss"), 
						Criteria.where("description").regex(".*misses 3-pt.*")));

		GroupOperation threePTAGroup = Aggregation.group("missPlayer").count().as("misses").first("missPlayer").as("player").first("matchId").as("matchId");

		ProjectionOperation threePTAProject = Aggregation.project().andExpression("player").as("player")
				.andExpression("misses").as("misses").andExpression("matchId").as("matchId");

		Aggregation threePTAAggregation = Aggregation.newAggregation(threePTAMatch, threePTAGroup, threePTAProject);

		AggregationResults<Document> threePTAResult = mongoTemplate.aggregate(threePTAAggregation, "play_by_play_actions", Document.class);

		List<Document> threePTAResultList = threePTAResult.getMappedResults();		


		for (PlayerStats playerStats : playersStats) {
			for (Document document : resultList2) {
				Document player = (Document) document.get("player");
				if (playerStats.getPlayer().getId() == Long.parseLong(player.get("_id").toString())) {
					playerStats.setFieldGoalAttempts(playerStats.getFieldGoals() + document.getInteger("misses"));
				}
			}
			
			for (Document document : threePointPlayersStats) {
				Document player = (Document) document.get("player");
				if (playerStats.getPlayer().getId() == Long.parseLong(player.get("_id").toString())) {
					playerStats.setThreePointFieldGoals(document.getInteger("threePointFieldGoals"));
				}
			}
			
			for (Document document : threePTAResultList) {
				Document player = (Document) document.get("player");
				if (playerStats.getPlayer().getId() == Long.parseLong(player.get("_id").toString())) {
					playerStats.setThreePointFieldGoalAttempts(playerStats.getThreePointFieldGoals() + document.getInteger("misses"));
				}
			}
		}
		
		return playersStats;
	}

}
