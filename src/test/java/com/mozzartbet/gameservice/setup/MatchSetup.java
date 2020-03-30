package com.mozzartbet.gameservice.setup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.mapper.SeasonMapper;
import com.mozzartbet.gameservice.parser.MatchPageHtmlParser;
import com.mozzartbet.gameservice.parser.PlayerPageHtmlParser;
import com.mozzartbet.gameservice.service.MatchService;
import com.mozzartbet.gameservice.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MatchSetup {
	
	@Autowired
	SeasonMapper seasonMapper;
	
	@Autowired
	PlayerService playerService;
	
	@Autowired
	MatchService matchService;

	public List<Match> getSetup(){
		
		Match m1 = new MatchPageHtmlParser("201905160GSW").parse();

		log.info("Insert season");

		seasonMapper.insert(m1.getSeason());

		log.info("Insert players for match");

		List<Player> players1 = new ArrayList<>();
		players1.addAll(new PlayerPageHtmlParser("POR/2019").parse());
		players1.addAll(new PlayerPageHtmlParser("GSW/2019").parse());

		for (Player player : players1) {
			playerService.save(player);
		}

		log.info("Insert match");

		matchService.insert(m1);
		
		Match m2 = new MatchPageHtmlParser("201905190TOR").parse();

		log.info("Insert players for match");

		List<Player> players2 = new ArrayList<>();
		players2.addAll(new PlayerPageHtmlParser("MIL/2019").parse());
		players2.addAll(new PlayerPageHtmlParser("TOR/2019").parse());

		for (Player player : players2) {
			playerService.save(player);
		}

		log.info("Insert match");

		matchService.insert(m2);
		
		List<Match> ms = new ArrayList<>();
		ms.add(matchService.getById(m1.getId()));
		ms.add(matchService.getById(m2.getId()));
		return ms;
	}
	
}
