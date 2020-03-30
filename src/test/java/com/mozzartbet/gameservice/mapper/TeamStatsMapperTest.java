package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TeamStatsMapperTest extends BaseMapperTest {

	@Autowired
	TeamMapper teamMapper;

	@Autowired
	TeamStatsMapper teamStatsMapper;

	@Test
	void testCrud() {

		log.info("Adding a new team");

		Team t = Team.builder().name("Denver Nuggets").url("dn_url_id").build();
		assertEquals(teamMapper.insert(t), 1);
		Team t1 = Team.builder().name("Sacramento Kings").url("sk_url_id").build();
		assertEquals(teamMapper.insert(t1), 1);

		log.info("Adding team stats");

		TeamStats teamStats = TeamStats.builder().team(t).matchId(1L).fieldGoals(2).fieldGoalAttempts(3)
				.fieldGoalPercentage(0.323).threePointFieldGoals(3).threePointFieldGoalAttempts(4)
				.threePointFieldGoalPercentage(0.531).freeThrows(5).freeThrowAttempts(8).freeThrowPercentage(0.673)
				.offensiveRebounds(3).defensiveRebounds(4).totalRebounds(7).assists(3).steals(4).blocks(2).turnovers(1)
				.personalFouls(3).points(23).build();

		assertEquals(teamStatsMapper.insert(teamStats), 1);

		teamStatsMapper.insert(TeamStats.builder().team(t1).matchId(1L).fieldGoals(2).fieldGoalAttempts(3)
				.fieldGoalPercentage(0.323).threePointFieldGoals(3).threePointFieldGoalAttempts(4)
				.threePointFieldGoalPercentage(0.531).freeThrows(5).freeThrowAttempts(8).freeThrowPercentage(0.673)
				.offensiveRebounds(3).defensiveRebounds(4).totalRebounds(7).assists(3).steals(4).blocks(2).turnovers(1)
				.personalFouls(3).points(23).build());

		TeamStats ts2 = TeamStats.builder().team(t).matchId(2L).fieldGoals(3).fieldGoalAttempts(4)
				.fieldGoalPercentage(0.323).threePointFieldGoals(3).threePointFieldGoalAttempts(4)
				.threePointFieldGoalPercentage(0.531).freeThrows(5).freeThrowAttempts(8).freeThrowPercentage(0.673)
				.offensiveRebounds(3).defensiveRebounds(4).totalRebounds(7).assists(3).steals(4).blocks(2).turnovers(1)
				.personalFouls(3).points(23).build();

		assertEquals(teamStatsMapper.insert(ts2), 1);

		log.info("Get team stats by id");

		TeamStats ps = teamStatsMapper.getById(teamStats.getId());

		assertEquals(ps.getId(), teamStats.getId());
		assertEquals(ps.getTeam().getId(), t.getId());
		assertEquals(ps.getTeam().getUrl(), t.getUrl());
		assertEquals(ps.getTeam().getName(), t.getName());
		assertEquals(ps.getMatchId(), teamStats.getMatchId());
		assertEquals(ps.getFieldGoals(), teamStats.getFieldGoals());
		assertEquals(ps.getFieldGoalAttempts(), teamStats.getFieldGoalAttempts());
		assertEquals(ps.getFieldGoalPercentage(), teamStats.getFieldGoalPercentage(), 0.00001);
		assertEquals(ps.getThreePointFieldGoals(), teamStats.getThreePointFieldGoals());
		assertEquals(ps.getThreePointFieldGoalAttempts(), teamStats.getThreePointFieldGoalAttempts());
		assertEquals(ps.getThreePointFieldGoalPercentage(), teamStats.getThreePointFieldGoalPercentage(), 0.00001);
		assertEquals(ps.getFreeThrows(), teamStats.getFreeThrows());
		assertEquals(ps.getFreeThrowAttempts(), teamStats.getFreeThrowAttempts());
		assertEquals(ps.getFreeThrowPercentage(), teamStats.getFreeThrowPercentage(), 0.00001);
		assertEquals(ps.getOffensiveRebounds(), teamStats.getOffensiveRebounds());
		assertEquals(ps.getDefensiveRebounds(), teamStats.getDefensiveRebounds());
		assertEquals(ps.getTotalRebounds(), teamStats.getTotalRebounds());
		assertEquals(ps.getAssists(), teamStats.getAssists());
		assertEquals(ps.getSteals(), teamStats.getSteals());
		assertEquals(ps.getBlocks(), teamStats.getBlocks());
		assertEquals(ps.getTurnovers(), teamStats.getTurnovers());
		assertEquals(ps.getPersonalFouls(), teamStats.getPersonalFouls());
		assertEquals(ps.getPoints(), teamStats.getPoints());

		log.info("Get all team stats");

		List<TeamStats> allTeamStats = teamStatsMapper.getAll();
		assertEquals(allTeamStats.size(), 3);

		log.info("Get match team stats");

		List<TeamStats> matchTeamStats = teamStatsMapper.getForMatch(1L);
		assertEquals(matchTeamStats.size(), 2);

		log.info("Update");
		ts2.setPoints(5);
		assertEquals(teamStatsMapper.update(ts2), 1);
		assertEquals(teamStatsMapper.getById(ts2.getId()).getPoints(), 5);

		log.info("Delete");
		assertEquals(teamStatsMapper.deleteById(ts2.getId()), 1);
		assertEquals(teamStatsMapper.getAll().size(), 2);
	}

}