package com.mozzartbet.gameservice.service;

import static com.mozzartbet.gameservice.service.PlayerAssert.assertPlayer;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.exception.PlayerException;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;
import com.mozzartbet.gameservice.service.dto.PlayerSearchResponse;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlayerServiceTest extends BaseServiceTest {

	@Autowired
	private PlayerSetup playerSetup;

	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private CacheManager cacheManager;
	
	private List<Player> ps;
	
	@BeforeEach
	public void getPlayerSetup() {
		ps = playerSetup.getSetup();
	}

	@Test
	public void testSearchPlayers() {

		log.info("Search by number");

		PlayerSearchResponse response = playerService.searchPlayers(PlayerSearchRequest.builder().number("8").build());

		assertEquals(response.getPlayers().size(), 1);
		assertPlayer(ps.get(0), response.getPlayers().get(0));

		log.info("Search by position");

		Set<String> positions = new HashSet<>();
		positions.add("SG");
		response = playerService.searchPlayers(PlayerSearchRequest.builder().positions(positions).build());

		assertEquals(response.getPlayers().size(), 1);
		assertPlayer(ps.get(0), response.getPlayers().get(0));
		
		log.info("Search by team id");

		response = playerService.searchPlayers(PlayerSearchRequest.builder().teamId(ps.get(0).getTeam().getId()).build());

		assertEquals(response.getPlayers().size(), 2);
		assertPlayer(ps.get(0), response.getPlayers().get(0));
		assertPlayer(ps.get(1), response.getPlayers().get(1));
		
	}
	
	@Test
	public void testSave() {

		Player p3 = Player.builder().name("Test Player").url("test/url").team(ps.get(0).getTeam()).number("33").college("Test College").position("SG").build();
		p3 = playerService.save(p3);
		assertNotNull(p3);
		
		p3.setName("Updated Name");
		p3.setNumber("11");
		p3.setUrl("updated/url");
		p3 = playerService.save(p3);
		
		assertEquals("Updated Name", p3.getName());
		assertEquals("11", p3.getNumber());
		assertEquals("updated/url", p3.getUrl());
		
		assertEquals(3, playerService.getAll().size());
		
	}
	
	@Test
	public void testGetById_CaffeineCached() {

		for (int i = 0; i < 3; i++) {
			Player p1 = playerService.getById(ps.get(0).getId());
			Player p2 = playerService.getById(ps.get(1).getId());
			
			assertEquals(p1.getId(), ps.get(0).getId());
			assertEquals(p2.getId(), ps.get(1).getId());
		}
		
		Cache cache = cacheManager.getCache("players");
		
		assertEquals(((CaffeineCache) cache).getNativeCache().estimatedSize(), 2);

		//stats
		assertEquals(((CaffeineCache) cache).getNativeCache().stats().requestCount(), 6);
		assertEquals(((CaffeineCache) cache).getNativeCache().stats().missCount(), 2);
		assertEquals(((CaffeineCache) cache).getNativeCache().stats().hitCount(), 4);
	}
	
	@Test
	public void testDuplicateException() {
		
		ps.get(0).setId(null);
		
		assertThrows(PlayerException.class, () -> {
			playerService.save(ps.get(0));
		});
	}
	
}