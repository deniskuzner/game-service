package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.statistics.linescore.AssistLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.LineScoreLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ReboundingLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringLeader;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LineScoreLeaderMapperTest extends BaseMapperTest {

	@Autowired
	TeamMapper teamMapper;

	@Autowired
	PlayerMapper playerMapper;

	@Autowired
	LineScoreLeaderMapper lineScoreLeaderMapper;

	@Autowired
	PlayerSetup playerSetup;

	@Test
	void testCrud() {

		List<Player> ps = playerSetup.getSetup();

		Player p1 = ps.get(0);
		Player p2 = ps.get(1);

		log.info("Insert line score leaders");

		assertEquals(lineScoreLeaderMapper.insert(ScoringLeader.builder().player(p1).points(5).quarter("1st Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(ScoringLeader.builder().player(p2).points(7).quarter("2nd Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(ScoringLeader.builder().player(p2).points(12).quarter("3rd Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(ScoringLeader.builder().player(p2).points(12).quarter("4th Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(AssistLeader.builder().player(p2).assists(4).quarter("1st Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(AssistLeader.builder().player(p1).assists(3).quarter("2nd Q").matchId(2L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(ReboundingLeader.builder().player(p2).rebounds(2).quarter("1st Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(ReboundingLeader.builder().player(p2).rebounds(3).quarter("2nd Q").matchId(1L).build()), 1);
		assertEquals(lineScoreLeaderMapper.insert(ReboundingLeader.builder().player(p1).rebounds(5).quarter("3rd Q").matchId(1L).build()), 1);

		log.info("Get all leaders");

		assertEquals(lineScoreLeaderMapper.getAll().size(), 9);

		log.info("Get leader by id");

		LineScoreLeader scoringLeader = lineScoreLeaderMapper.getById(1L);
		assertEquals(scoringLeader.getClass(), ScoringLeader.class);
		assertEquals(((ScoringLeader) scoringLeader).getMatchId(), 1L);
		assertEquals(((ScoringLeader) scoringLeader).getPlayer().getId(), p1.getId());
		assertEquals(((ScoringLeader) scoringLeader).getPlayer().getName(), p1.getName());
		assertEquals(((ScoringLeader) scoringLeader).getPlayer().getUrl(), p1.getUrl());
		assertEquals(((ScoringLeader) scoringLeader).getPoints(), 5);
		assertEquals(((ScoringLeader) scoringLeader).getQuarter(), "1st Q");

		LineScoreLeader assistLeader = lineScoreLeaderMapper.getById(5L);
		assertEquals(assistLeader.getClass(), AssistLeader.class);
		assertEquals(((AssistLeader) assistLeader).getMatchId(), 1L);
		assertEquals(((AssistLeader) assistLeader).getPlayer().getId(), p2.getId());
		assertEquals(((AssistLeader) assistLeader).getPlayer().getName(), p2.getName());
		assertEquals(((AssistLeader) assistLeader).getPlayer().getUrl(), p2.getUrl());
		assertEquals(((AssistLeader) assistLeader).getAssists(), 4);
		assertEquals(((AssistLeader) assistLeader).getQuarter(), "1st Q");

		LineScoreLeader reboundingLeader = lineScoreLeaderMapper.getById(7L);
		assertEquals(reboundingLeader.getClass(), ReboundingLeader.class);
		assertEquals(((ReboundingLeader) reboundingLeader).getMatchId(), 1L);
		assertEquals(((ReboundingLeader) reboundingLeader).getPlayer().getId(), p2.getId());
		assertEquals(((ReboundingLeader) reboundingLeader).getPlayer().getName(), p2.getName());
		assertEquals(((ReboundingLeader) reboundingLeader).getPlayer().getUrl(), p2.getUrl());
		assertEquals(((ReboundingLeader) reboundingLeader).getRebounds(), 2);
		assertEquals(((ReboundingLeader) reboundingLeader).getQuarter(), "1st Q");
		
		log.info("Get linescore leaders for match by type");

		List<ScoringLeader> scoringLeaders = new ArrayList<>();

		for (LineScoreLeader lineScoreLeader : lineScoreLeaderMapper.getForMatchByType(1l, "scoring_leader")) {
			scoringLeaders.add((ScoringLeader) lineScoreLeader);
		}

		assertEquals(scoringLeaders.size(), 4);

		List<ReboundingLeader> reboundingLeaders = new ArrayList<>();

		for (LineScoreLeader lineScoreLeader : lineScoreLeaderMapper.getForMatchByType(1l, "rebounding_leader")) {
			reboundingLeaders.add((ReboundingLeader) lineScoreLeader);
		}

		assertEquals(reboundingLeaders.size(), 3);

		List<AssistLeader> assistLeaders = new ArrayList<>();

		for (LineScoreLeader lineScoreLeader : lineScoreLeaderMapper.getForMatchByType(1l, "assist_leader")) {
			assistLeaders.add((AssistLeader) lineScoreLeader);
		}

		assertEquals(assistLeaders.size(), 1);

		log.info("Update leader");

		assertEquals(lineScoreLeaderMapper.update(ScoringLeader.builder().id(1L).player(p1).points(15).quarter("2nd Q").matchId(1L).build()), 1);
		assertEquals(((ScoringLeader) lineScoreLeaderMapper.getById(1l)).getPoints(), 15);
		assertEquals(((ScoringLeader) lineScoreLeaderMapper.getById(1l)).getQuarter(), "2nd Q");

		log.info("Delete leader");

		assertEquals(lineScoreLeaderMapper.deleteById(1L), 1);
		assertEquals(lineScoreLeaderMapper.getById(1l), null);

	}

}
