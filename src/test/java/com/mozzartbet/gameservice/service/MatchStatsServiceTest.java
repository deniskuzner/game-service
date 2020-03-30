package com.mozzartbet.gameservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.MatchStats;
import com.mozzartbet.gameservice.setup.MatchSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MatchStatsServiceTest extends BaseServiceTest {

	@Autowired
	MatchStatsService matchStatsService;
	
	@Autowired
	MatchSetup matchSetup;
	
	@Test
	void testCrud() {
		
		List<Match> ms = matchSetup.getSetup();
		
		log.info("Insert match stats");
		
		MatchStats matchStats = matchStatsService.insert(ms.get(0));
		
		assertNotNull(matchStats);
		assertNotNull(matchStats.getPlayerStats());
		assertNotNull(matchStats.getTeamStats());
		assertNotNull(matchStats.getLineScore());
		assertNotNull(matchStats.getLineScore().getScoringQuarters());
		assertNotNull(matchStats.getLineScore().getScoringLeaders());
		assertNotNull(matchStats.getLineScore().getAssistLeaders());
		assertNotNull(matchStats.getLineScore().getReboundingLeaders());
		
		assertNotEquals(matchStats.getPlayerStats().size(), 0);
		assertEquals(matchStats.getTeamStats().size(), 2);
		assertEquals(matchStats.getLineScore().getScoringQuarters().size(), 5);
		assertEquals(matchStats.getLineScore().getScoringLeaders().size(), 5);
		assertEquals(matchStats.getLineScore().getAssistLeaders().size(), 7);
		assertEquals(matchStats.getLineScore().getReboundingLeaders().size(), 14);
		
		log.info("Delete stats by matchId");
		
		matchStatsService.deleteByMatchId(ms.get(0).getId());
		
		MatchStats deletedStats = matchStatsService.getByMatchId(ms.get(0).getId());
		assertEquals(deletedStats.getPlayerStats().size(), 0);
		assertEquals(deletedStats.getTeamStats().size(), 0);
		assertEquals(deletedStats.getLineScore().getScoringQuarters().size(), 0);
		assertEquals(deletedStats.getLineScore().getScoringLeaders().size(), 0);
		assertEquals(deletedStats.getLineScore().getAssistLeaders().size(), 0);
		assertEquals(deletedStats.getLineScore().getReboundingLeaders().size(), 0);
		
	}
	
}