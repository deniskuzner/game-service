package com.mozzartbet.gameservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.TeamException;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TeamServiceTest extends BaseServiceTest {
	
	@Autowired
	TeamService teamService;

	@Autowired
	private PlayerSetup playerSetup;

	@Test
	public void testSave() {

		log.info("Adding a new team");
		Team t = Team.builder().name("Milwaukee Bucks").url("MIL/2019").build();
		t = teamService.save(t);

		assertNotNull(t);
		assertEquals(t.getName(), "Milwaukee Bucks");

		t.setName("Updated name");
		t.setUrl("Updated url");

		t = teamService.save(t);

		assertEquals(t.getName(), "Updated name");
		assertEquals(t.getUrl(), "Updated url");

		assertEquals(teamService.getAll().size(), 1);
	}

	@Test
	public void testDuplicateException() {
		List<Player> ps = playerSetup.getSetup();

		Team t = ps.get(0).getTeam();
		t.setId(null);

		assertThrows(TeamException.class, () -> {
			teamService.save(t);
		});
	}
	
}
