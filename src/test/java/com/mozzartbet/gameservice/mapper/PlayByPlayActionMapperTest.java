package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.dao.MatchDao;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;
import com.mozzartbet.gameservice.service.dto.FindPbpActionsRequest;
import com.mozzartbet.gameservice.setup.PlayByPlayActionSetup;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlayByPlayActionMapperTest extends BaseMapperTest {

	@Autowired
	TeamMapper teamMapper;

	@Autowired
	PlayerMapper playerMapper;

	@Autowired
	PlayByPlayActionMapper playByPlayActionMapper;

	@Autowired
	MatchMapper matchMapper;

	@Autowired
	SeasonMapper seasonMapper;
	
	@Autowired
	MatchDao matchDao;
	
	@Autowired
	PlayerSetup playerSetup;
	
	@Autowired
	PlayByPlayActionSetup pbpActionSetup;

	@Test
	void testCrud() {
		
		List<Player> ps = playerSetup.getSetup();
		Team t = ps.get(0).getTeam();
		
		log.info("Getting actions");

		List<PlayByPlayAction> actions = pbpActionSetup.getSetup(ps);
		
		log.info("Point action");
		PlayByPlayAction a1 = playByPlayActionMapper.getById(actions.get(0).getId());

		assertEquals(a1.getClass(), Point.class);
		assertEquals(((Point) a1).getPointPlayer().getId(), ps.get(0).getId());
		assertEquals(((Point) a1).getPointPlayer().getUrl(), ps.get(0).getUrl());
		assertEquals(((Point) a1).getPointPlayer().getName(), ps.get(0).getName());
		assertEquals(a1.getTimestamp(), LocalTime.parse("00:12:00.0"));
		assertEquals(a1.getDescription(), "description");
		assertEquals(a1.getSumScore(), "2:0");
		assertEquals(a1.getQuarter(), "1st Q");
		assertEquals(a1.getTeam().getId(), t.getId());
		assertEquals(a1.getTeam().getName(), t.getName());
		assertEquals(a1.getTeam().getUrl(), t.getUrl());
		assertEquals(a1.getMatchId(), actions.get(0).getMatchId());

		log.info("Rebound action");
		PlayByPlayAction a2 = playByPlayActionMapper.getById(actions.get(1).getId());

		assertEquals(a2.getClass(), Rebound.class);
		assertEquals(((Rebound) a2).getPlayer().getId(), ps.get(1).getId());
		assertEquals(((Rebound) a2).getPlayer().getUrl(), ps.get(1).getUrl());
		assertEquals(((Rebound) a2).getPlayer().getName(), ps.get(1).getName());
		assertEquals(a2.getTimestamp(), LocalTime.parse("00:11:55.0"));
		assertEquals(a2.getDescription(), "description");
		assertEquals(a2.getSumScore(), "2:3");
		assertEquals(a2.getQuarter(), "1st Q");
		assertEquals(a2.getTeam().getId(), t.getId());
		assertEquals(a2.getTeam().getName(), t.getName());
		assertEquals(a2.getTeam().getUrl(), t.getUrl());
		assertEquals(a2.getMatchId(), actions.get(1).getMatchId());

		log.info("Foul action");
		PlayByPlayAction a3 = playByPlayActionMapper.getById(actions.get(2).getId());

		assertEquals(a3.getClass(), Foul.class);
		assertEquals(((Foul) a3).getFoulByPlayer().getId(), ps.get(0).getId());
		assertEquals(((Foul) a3).getFoulByPlayer().getUrl(), ps.get(0).getUrl());
		assertEquals(((Foul) a3).getFoulByPlayer().getName(), ps.get(0).getName());
		assertEquals(a3.getTimestamp(), LocalTime.parse("00:11:50.0"));
		assertEquals(a3.getDescription(), "foul description");
		assertEquals(a3.getSumScore(), "5:3");
		assertEquals(a3.getQuarter(), "1st Q");
		assertEquals(a3.getTeam().getId(), t.getId());
		assertEquals(a3.getTeam().getName(), t.getName());
		assertEquals(a3.getTeam().getUrl(), t.getUrl());
		assertEquals(a3.getMatchId(), actions.get(2).getMatchId());

		log.info("Miss action");
		PlayByPlayAction a4 = playByPlayActionMapper.getById(actions.get(3).getId());

		assertEquals(a4.getClass(), Miss.class);
		assertEquals(((Miss) a4).getMissPlayer().getId(), ps.get(0).getId());
		assertEquals(((Miss) a4).getMissPlayer().getUrl(), ps.get(0).getUrl());
		assertEquals(((Miss) a4).getMissPlayer().getName(), ps.get(0).getName());
		assertEquals(((Miss) a4).getBlockPlayer().getName(), ps.get(1).getName());
		assertEquals(((Miss) a4).getBlockPlayer().getName(), ps.get(1).getName());
		assertEquals(((Miss) a4).getBlockPlayer().getName(), ps.get(1).getName());
		assertEquals(a4.getTimestamp(), LocalTime.parse("00:04:04.0"));
		assertEquals(a4.getDescription(), "miss description");
		assertEquals(a4.getSumScore(), "21:45");
		assertEquals(a4.getQuarter(), "3rd Q");
		assertEquals(a4.getTeam().getId(), t.getId());
		assertEquals(a4.getTeam().getName(), t.getName());
		assertEquals(a4.getTeam().getUrl(), t.getUrl());
		assertEquals(a4.getMatchId(), actions.get(3).getMatchId());

		log.info("Substitution action");
		PlayByPlayAction a5 = playByPlayActionMapper.getById(actions.get(4).getId());

		assertEquals(a5.getClass(), Substitution.class);
		assertEquals(((Substitution) a5).getInPlayer().getId(), ps.get(0).getId());
		assertEquals(((Substitution) a5).getInPlayer().getUrl(), ps.get(0).getUrl());
		assertEquals(((Substitution) a5).getInPlayer().getName(), ps.get(0).getName());
		assertEquals(((Substitution) a5).getOutPlayer().getName(), ps.get(1).getName());
		assertEquals(((Substitution) a5).getOutPlayer().getName(), ps.get(1).getName());
		assertEquals(((Substitution) a5).getOutPlayer().getName(), ps.get(1).getName());
		assertEquals(a5.getTimestamp(), LocalTime.parse("00:07:34.0"));
		assertEquals(a5.getDescription(), "substitution description");
		assertEquals(a5.getSumScore(), "44:56");
		assertEquals(a5.getQuarter(), "3rd Q");
		assertEquals(a5.getTeam().getId(), t.getId());
		assertEquals(a5.getTeam().getName(), t.getName());
		assertEquals(a5.getTeam().getUrl(), t.getUrl());

		log.info("Turnover action");
		PlayByPlayAction a6 = playByPlayActionMapper.getById(actions.get(5).getId());

		assertEquals(a6.getClass(), Turnover.class);
		assertEquals(((Turnover) a6).getTurnoverPlayer().getId(), ps.get(0).getId());
		assertEquals(((Turnover) a6).getTurnoverPlayer().getUrl(), ps.get(0).getUrl());
		assertEquals(((Turnover) a6).getTurnoverPlayer().getName(), ps.get(0).getName());
		assertEquals(((Turnover) a6).getStealPlayer().getName(), ps.get(1).getName());
		assertEquals(((Turnover) a6).getStealPlayer().getName(), ps.get(1).getName());
		assertEquals(((Turnover) a6).getStealPlayer().getName(), ps.get(1).getName());
		assertEquals(a6.getTimestamp(), LocalTime.parse("00:02:35.0"));
		assertEquals(a6.getDescription(), "turnover description");
		assertEquals(a6.getSumScore(), "48:67");
		assertEquals(a6.getQuarter(), "4th Q");
		assertEquals(a6.getTeam().getId(), t.getId());
		assertEquals(a6.getTeam().getName(), t.getName());
		assertEquals(a6.getTeam().getUrl(), t.getUrl());

		log.info("Get all");
		List<PlayByPlayAction> allActions = playByPlayActionMapper.getAll();
		assertEquals(actions.size(), allActions.size());
		
		log.info("Find by timestamp");
		List<PlayByPlayAction> actionsByTimestamp = playByPlayActionMapper.findPbpActions(FindPbpActionsRequest.builder()
				.fromDate(LocalTime.parse("00:04:04.0"))
				.toDate(LocalTime.parse("00:12:00.0"))
				.quarter("1st Q")
				.matchId(a1.getMatchId()).build(), 0);
		assertNotNull(actionsByTimestamp);
		assertEquals(actionsByTimestamp.size(), 3);
		
		List<PlayByPlayAction> actionsByMatch = playByPlayActionMapper.findPbpActions(FindPbpActionsRequest.builder()
				.matchId(a1.getMatchId()).build(), 0);
		assertNotNull(actionsByMatch);
		assertEquals(actionsByMatch.size(), 3);
		
		log.info("Update action");
		a6.setDescription("updated turnover description");
		a6.setSumScore("59:78");
		a6.setQuarter("1st OT");
		a6.setTimestamp(LocalTime.parse("00:01:35.0"));
		playByPlayActionMapper.update(a6);
		PlayByPlayAction a7 = playByPlayActionMapper.getById(a6.getId());
		assertEquals(a7.getClass(), Turnover.class);
		assertEquals(((Turnover) a7).getTurnoverPlayer().getId(), ps.get(0).getId());
		assertEquals(((Turnover) a7).getTurnoverPlayer().getUrl(), ps.get(0).getUrl());
		assertEquals(((Turnover) a7).getTurnoverPlayer().getName(), ps.get(0).getName());
		assertEquals(((Turnover) a7).getStealPlayer().getName(), ps.get(1).getName());
		assertEquals(((Turnover) a7).getStealPlayer().getName(), ps.get(1).getName());
		assertEquals(((Turnover) a7).getStealPlayer().getName(), ps.get(1).getName());
		assertEquals(a7.getTimestamp(), LocalTime.parse("00:01:35.0"));
		assertEquals(a7.getDescription(), "updated turnover description");
		assertEquals(a7.getSumScore(), "59:78");
		assertEquals(a7.getQuarter(), "1st OT");
		assertEquals(a7.getTeam().getId(), t.getId());
		assertEquals(a7.getTeam().getName(), t.getName());
		assertEquals(a7.getTeam().getUrl(), t.getUrl());
		
		log.info("Get actions by match id");
		assertEquals(3, playByPlayActionMapper.getByMatchId(2L).size());

		log.info("Delete actions");
		
		actions.forEach(a -> {
			assertEquals(playByPlayActionMapper.deleteById(a.getId()), 1);
		});

	}

}