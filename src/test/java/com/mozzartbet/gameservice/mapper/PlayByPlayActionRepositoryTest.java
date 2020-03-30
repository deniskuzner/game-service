package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mozzartbet.gameservice.dao.PlayByPlayActionRepository;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.dto.TeamQuarterPoints;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlayByPlayActionRepositoryTest extends BaseMapperTest {

	@Autowired
	PlayByPlayActionRepository playByPlayActionRepository;

	@Autowired
	PlayerSetup playerSetup;

	@Autowired
	MongoTemplate mongoTemplate;

	@Test
	void test() {

		List<Player> ps = playerSetup.getSetup();

		Player p1 = ps.get(0);
		Player p2 = ps.get(1);
		Team t = p1.getTeam();

		Team t1 = Team.builder().id(1L).name("Tim 2").url("tim2").build();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:m:s.S");

		log.info("Saving actions");
		List<PlayByPlayAction> actions = new ArrayList<PlayByPlayAction>();
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:12:00.0", dateTimeFormatter))
				.description("description").quarter("1st Q").sumScore("2:0").team(t).points(1).pointPlayer(p1)
				.assistPlayer(p2).build());
		actions.add(Rebound.builder().matchId(1L).player(p2).description("description")
				.timestamp(LocalTime.parse("00:11:55.0", dateTimeFormatter)).sumScore("2:3").quarter("1st Q").team(t)
				.build());
		actions.add(Foul.builder().matchId(1L).timestamp(LocalTime.parse("00:11:50.0", dateTimeFormatter))
				.description("foul description").quarter("1st Q").sumScore("5:3").foulByPlayer(p1).team(t).build());
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:10:00.0", dateTimeFormatter))
				.description("description").quarter("1st Q").sumScore("8:3").team(t1).points(3).pointPlayer(p2)
				.assistPlayer(p2).build());
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:9:00.0", dateTimeFormatter))
				.description("description").quarter("1st Q").sumScore("10:3").team(t).points(2).pointPlayer(p1)
				.assistPlayer(p2).build());
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:9:00.0", dateTimeFormatter))
				.description("description").quarter("2nd Q").sumScore("10:5").team(t1).points(1).pointPlayer(p2)
				.assistPlayer(p2).build());
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:9:00.0", dateTimeFormatter))
				.description("description").quarter("2nd Q").sumScore("12:5").team(t).points(2).pointPlayer(p1)
				.assistPlayer(p2).build());
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:9:00.0", dateTimeFormatter))
				.description("description").quarter("2nd Q").sumScore("12:7").team(t1).points(2).pointPlayer(p2)
				.assistPlayer(p2).build());
		actions.add(Miss.builder().matchId(1L).timestamp(LocalTime.parse("00:04:04.0", dateTimeFormatter))
				.description("misses 2-pt").quarter("3rd Q").sumScore("21:45").missPlayer(p1).blockPlayer(p2).team(t)
				.build());
		actions.add(Miss.builder().matchId(1L).timestamp(LocalTime.parse("00:04:04.0", dateTimeFormatter))
				.description("misses 3-pt").quarter("3rd Q").sumScore("21:45").missPlayer(p1).blockPlayer(p2).team(t)
				.build());
		actions.add(Miss.builder().matchId(1L).timestamp(LocalTime.parse("00:04:04.0", dateTimeFormatter))
				.description("misses free").quarter("3rd Q").sumScore("21:45").missPlayer(p1).blockPlayer(p2).team(t)
				.build());
		actions.add(Miss.builder().matchId(1L).timestamp(LocalTime.parse("00:04:04.0", dateTimeFormatter))
				.description("misses 2-pt").quarter("3rd Q").sumScore("21:45").missPlayer(p2).blockPlayer(p2).team(t)
				.build());
		actions.add(Substitution.builder().matchId(2L).timestamp(LocalTime.parse("00:07:34.0", dateTimeFormatter))
				.description("substitution description").quarter("3rd Q").sumScore("44:56").inPlayer(p1).outPlayer(p2)
				.team(t).build());
		actions.add(Turnover.builder().matchId(2L).timestamp(LocalTime.parse("00:02:35.0", dateTimeFormatter))
				.description("turnover description").quarter("4th Q").sumScore("48:67").turnoverPlayer(p1)
				.stealPlayer(p2).team(t).build());

		actions.forEach(a -> {
			playByPlayActionRepository.insert(a);
		});

		assertEquals(playByPlayActionRepository.count(), 14);

		MatchOperation matchOperation = Aggregation.match(new Criteria().andOperator(Criteria.where("matchId").is(1L),
				Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Point")));

		GroupOperation groupOperation = Aggregation.group("quarter", "team").sum("points").as("quarterPoints")
				.last("matchId").as("matchId");

		ProjectionOperation projectToMatchModel = Aggregation.project().andExpression("quarter").as("quarter")
				.andExpression("quarterPoints").as("points").andExpression("matchId").as("matchId");

		Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectToMatchModel);

		AggregationResults<TeamQuarterPoints> result = mongoTemplate.aggregate(aggregation, "play_by_play_actions",
				TeamQuarterPoints.class);

		List<TeamQuarterPoints> resultList = result.getMappedResults();

		assertEquals(resultList.size(), 4);

		// FG

		MatchOperation matchOperation1 = Aggregation.match(new Criteria().andOperator(Criteria.where("matchId").is(1L),
				Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Point"),
				Criteria.where("points").gte(2)));

		GroupOperation groupOperation1 = Aggregation.group("pointPlayer").count().as("FG").first("matchId")
				.as("matchId").first("pointPlayer").as("player");

		ProjectionOperation projectToMatchModel1 = Aggregation.project().andExpression("player").as("player")
				.andExpression("FG").as("fieldGoals").andExpression("matchId").as("matchId").andExclude("_id");

		Aggregation aggregation1 = Aggregation.newAggregation(matchOperation1, groupOperation1, projectToMatchModel1);

		AggregationResults<PlayerStats> result1 = mongoTemplate.aggregate(aggregation1, "play_by_play_actions", PlayerStats.class);

		List<PlayerStats> playersStats = result1.getMappedResults();

		// MISS

		MatchOperation matchOperation2 = Aggregation.match(new Criteria()
				.andOperator(Criteria.where("matchId").is(1L),
						Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Miss"))
				.orOperator(Criteria.where("description").regex(".*misses 2-pt.*"),
						Criteria.where("description").regex(".*misses 3-pt.*")));

		GroupOperation groupOperation2 = Aggregation.group("missPlayer").count().as("misses").first("missPlayer")
				.as("player").first("matchId").as("matchId");

		ProjectionOperation projectToMatchModel2 = Aggregation.project().andExpression("player").as("player")
				.andExpression("misses").as("misses").andExpression("matchId").as("matchId");

		Aggregation aggregation2 = Aggregation.newAggregation(matchOperation2, groupOperation2, projectToMatchModel2);

		AggregationResults<Document> result2 = mongoTemplate.aggregate(aggregation2, "play_by_play_actions",
				Document.class);

		List<Document> resultList2 = result2.getMappedResults();

		assertEquals(resultList2.size(), 2);
		
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
				.andOperator(Criteria.where("matchId").is(1L), Criteria.where("_class").is("com.mozzartbet.gameservice.domain.playbyplay.Miss"), 
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
			
			System.out.println(playerStats);
		}

	}

}
