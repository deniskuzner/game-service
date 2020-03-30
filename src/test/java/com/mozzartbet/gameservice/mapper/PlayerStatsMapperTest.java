package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlayerStatsMapperTest extends BaseMapperTest {
	
	@Autowired
	private PlayerStatsMapper playerStatsMapper;
	
	@Autowired
	private PlayerSetup playerSetup;

	@Test
	void testCrud() {
		
		Player p = playerSetup.getSetup().get(0);
		
		log.info("Adding player stats");
		
		PlayerStats playerStats = PlayerStats.builder().player(p).matchId(1L).fieldGoals(2).fieldGoalAttempts(3).fieldGoalPercentage(0.323)
				.threePointFieldGoals(3).threePointFieldGoalAttempts(4).threePointFieldGoalPercentage(0.531)
				.freeThrows(5).freeThrowAttempts(8).freeThrowPercentage(0.673)
				.offensiveRebounds(3).defensiveRebounds(4).totalRebounds(7).assists(3).steals(4).blocks(2)
				.turnovers(1).personalFouls(3).points(23).build();
		
		assertEquals(playerStatsMapper.insert(playerStats), 1);
		
		PlayerStats ps2 = PlayerStats.builder().player(p).matchId(2L).fieldGoals(3).fieldGoalAttempts(4).fieldGoalPercentage(0.323)
		.threePointFieldGoals(3).threePointFieldGoalAttempts(4).threePointFieldGoalPercentage(0.531)
		.freeThrows(5).freeThrowAttempts(8).freeThrowPercentage(0.673)
		.offensiveRebounds(3).defensiveRebounds(4).totalRebounds(7).assists(3).steals(4).blocks(2)
		.turnovers(1).personalFouls(3).points(23).build();

		assertEquals(playerStatsMapper.insert(ps2), 1);
		
		log.info("Get player stats by id");
		
		PlayerStats ps = playerStatsMapper.getById(playerStats.getId());
		
		assertEquals(ps.getId(), playerStats.getId());
		assertEquals(ps.getPlayer().getId(), p.getId());
		assertEquals(ps.getPlayer().getUrl(), p.getUrl());
		assertEquals(ps.getPlayer().getName(), p.getName());
		assertEquals(ps.getMatchId(), playerStats.getMatchId());
		assertEquals(ps.getFieldGoals(), playerStats.getFieldGoals());
		assertEquals(ps.getFieldGoalAttempts(), playerStats.getFieldGoalAttempts());
		assertEquals(ps.getFieldGoalPercentage(), playerStats.getFieldGoalPercentage(), 0.00001);
		assertEquals(ps.getThreePointFieldGoals(), playerStats.getThreePointFieldGoals());
		assertEquals(ps.getThreePointFieldGoalAttempts(), playerStats.getThreePointFieldGoalAttempts());
		assertEquals(ps.getThreePointFieldGoalPercentage(), playerStats.getThreePointFieldGoalPercentage(), 0.00001);
		assertEquals(ps.getFreeThrows(), playerStats.getFreeThrows());
		assertEquals(ps.getFreeThrowAttempts(), playerStats.getFreeThrowAttempts());
		assertEquals(ps.getFreeThrowPercentage(), playerStats.getFreeThrowPercentage(), 0.00001);
		assertEquals(ps.getOffensiveRebounds(), playerStats.getOffensiveRebounds());
		assertEquals(ps.getDefensiveRebounds(), playerStats.getDefensiveRebounds());
		assertEquals(ps.getTotalRebounds(), playerStats.getTotalRebounds());
		assertEquals(ps.getAssists(), playerStats.getAssists());
		assertEquals(ps.getSteals(), playerStats.getSteals());
		assertEquals(ps.getBlocks(), playerStats.getBlocks());
		assertEquals(ps.getTurnovers(), playerStats.getTurnovers());
		assertEquals(ps.getPersonalFouls(), playerStats.getPersonalFouls());
		assertEquals(ps.getPoints(), playerStats.getPoints());
		
		log.info("Get all player stats");
		
		List<PlayerStats> allPlayerStats = playerStatsMapper.getAll();
		assertEquals(allPlayerStats.size(), 2);
		
		log.info("Get match player stats");
		
		List<PlayerStats> matchPlayerStats = playerStatsMapper.getStatsForMatch(1L);
		assertEquals(matchPlayerStats.size(), 1);
		
		log.info("Update");
		ps2.setPoints(5);
		assertEquals(playerStatsMapper.update(ps2), 1);
		assertEquals(playerStatsMapper.getById(ps2.getId()).getPoints(), 5);
		
		log.info("Delete");
		assertEquals(playerStatsMapper.deleteById(ps2.getId()), 1);
		assertEquals(playerStatsMapper.getAll().size(), 1);
	}

}