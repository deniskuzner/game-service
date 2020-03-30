package com.mozzartbet.gameservice.mapper;

import static com.mozzartbet.gameservice.service.PlayerAssert.assertPlayer;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlayerMapperTest extends BaseMapperTest {
	
	@Autowired
	private TeamMapper teamMapper;

	@Autowired
	private PlayerMapper playerMapper;
	
	@Autowired
	private PlayerSetup playerSetup;
	
	@Test
	public void testCrud() throws Exception {
		assertEquals(teamMapper.count(), 0L);
		assertEquals(playerMapper.count(), 0L);
		
		log.info("Adding a new team");
		Team t = Team.builder().name("Denver Nuggets").url("DEN/2019").build();
		assertEquals(teamMapper.insert(t), 1);
		
		log.info("Adding a new player");
		Player p = Player.builder().team(t).url("b/bledser01").name("Eric Bledsoe").number("6").position("PG").height("6-1").weight(205).birthDate("December 9, 1989").experience("9").college("University of Kentucky").build();
		assertEquals(playerMapper.insert(p), 1);
		
		log.info("Getting player with team");
		p = playerMapper.getById(p.getId());
		
		assertPlayer(p, Player.builder().team(t).url("b/bledser01").name("Eric Bledsoe").number("6").position("PG").height("6-1").weight(205)
				.birthDate("December 9, 1989").experience("9").college("University of Kentucky").build());
		
		long id = p.getId();
		
		log.info("Updating a player");
		Player updatePlayer = Player.builder().id(id).team(t).url("NEW url").name("NEW NAME").number("7").position("PG").height("6-1").weight(205).birthDate("December 9, 1989").experience("9").college("University of Kentucky").build();
		assertEquals(playerMapper.update(updatePlayer), 1);
		
		updatePlayer = playerMapper.getById(id);
		assertEquals(updatePlayer.getName(), "NEW NAME");
		assertEquals(updatePlayer.getNumber(), "7");
		
		log.info("Deleting a player");
		assertEquals(playerMapper.deleteById(id), 1);
		assertEquals(playerMapper.getById(id), null);
		
		
		log.info("Getting all players");
		List<Player> ps = playerSetup.getSetup();
		List<Player> allPlayers = playerMapper.getAll();
		assertEquals(2, allPlayers.size());
		assertPlayer(ps.get(0), allPlayers.get(1));
		assertPlayer(ps.get(1), allPlayers.get(0));
		
	}

}