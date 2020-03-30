package com.mozzartbet.gameservice.setup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.mapper.PlayerMapper;
import com.mozzartbet.gameservice.mapper.TeamMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlayerSetup {

	@Autowired
	TeamMapper teamMapper;
	
	@Autowired
	PlayerMapper playerMapper;
	
	public List<Player> getSetup(){
		
		log.info("Adding a new team");
		Team t = Team.builder().name("Sacramento Kings").url("SAC/2019").build();
		teamMapper.insert(t);
		
		log.info("Adding players");
		Player p1 = Player.builder().team(t).url("b/bogdabo01").name("Bogdan Bogdanovic").number("8").position("SG").height("6-6").weight(205).birthDate("August 18, 1992").experience("1").college("").build();
		playerMapper.save(p1);

		Player p2 = Player.builder().team(t).url("b/bertada01").name("Davis Bertans").number("42").position("PF").height("6-10").weight(225).birthDate("November 12, 1992").experience("2").college("").build();
		playerMapper.save(p2);
		
		log.info("Fetching players for team");
		List<Player> ps = playerMapper.getPlayersForTeam(t.getId());
		
		return ps;
	
	}
}
