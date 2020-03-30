package com.mozzartbet.gameservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.cache.CacheStats;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.exception.MatchException;
import com.mozzartbet.gameservice.mapper.SeasonMapper;
import com.mozzartbet.gameservice.parser.MatchPageHtmlParser;
import com.mozzartbet.gameservice.parser.PlayerPageHtmlParser;
import com.mozzartbet.gameservice.service.impl.MatchServiceImpl;
import com.mozzartbet.gameservice.setup.MatchSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MatchServiceTest extends BaseServiceTest {

	@Autowired
	MatchService matchService;

	@Autowired
	SeasonMapper seasonMapper;

	@Autowired
	PlayerService playerService;

	@Autowired
	MatchSetup matchSetup;

	@Test
	public void testCrud() {
		Match m = new MatchPageHtmlParser("201905160GSW").parse();

		log.info("Insert season");

		seasonMapper.insert(m.getSeason());

		log.info("Insert players for match");

		List<Player> players = new ArrayList<>();
		players.addAll(new PlayerPageHtmlParser("POR/2019").parse());
		players.addAll(new PlayerPageHtmlParser("GSW/2019").parse());

		for (Player player : players) {
			playerService.save(player);
		}

		log.info("Insert match");

		m = matchService.insert(m);

		assertNotNull(m);
		assertEquals(m.getHost().getName(), "Portland Trail Blazers");
		assertEquals(m.getGuest().getName(), "Golden State Warriors");
		assertEquals(m.getResult(), "111-114");
		assertNotNull(m.getPbpActions());
		assertNotEquals(m.getPbpActions().size(), 0);

		m.setResult("120-140");
		m = matchService.update(m);

		assertNotNull(m);
		assertEquals(m.getResult(), "120-140");

		assertEquals(matchService.deleteById(m.getId()), 1);

		assertEquals(matchService.getAll().size(), 0);
	}


	@Test
	public void testGetMatch_GuavaCached() {
		
		List<Match> ms = matchSetup.getSetup();

		for (int i = 1; i <= 3; i++) {
			Match m1 = matchService.getMatch(ms.get(0).getId());
			Match m2 = matchService.getMatch(ms.get(1).getId());

			assertEquals(m1.getId(), ms.get(0).getId());
			assertEquals(m2.getId(), ms.get(1).getId());
		}
		
		CacheStats cs = ((MatchServiceImpl) matchService).matchCacheStats();
		assertEquals(cs.missCount(), 2);
		assertEquals(cs.hitCount(), 4);
		assertEquals(cs.loadSuccessCount(), 2);
	}
	
	@Test
	public void testDuplicateException() {
		
		List<Match> ms = matchSetup.getSetup();
		
		ms.get(0).setId(null);
		
		assertThrows(MatchException.class, () -> {
			matchService.insert(ms.get(0));
		});
	}
}
