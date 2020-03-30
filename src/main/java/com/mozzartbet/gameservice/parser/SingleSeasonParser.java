package com.mozzartbet.gameservice.parser;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Season;

public class SingleSeasonParser {

	private String season;
	private SeasonMatchPageHtmlParser seasonMatchPageHtmlParser;
	private SeasonPlayerPageHtmlParser seasonPlayerPageHtmlParser;
	
	public SingleSeasonParser(String season) {
		this.season = season;
		seasonMatchPageHtmlParser = new SeasonMatchPageHtmlParser(season);
		seasonPlayerPageHtmlParser = new SeasonPlayerPageHtmlParser(season);
	}
	
	public Season parse() {
		List<Match> matches = seasonMatchPageHtmlParser.parse();
		List<Player> players = seasonPlayerPageHtmlParser.parse();
		
		return Season.builder().year(Integer.parseInt(season.split("_")[1])).league(season.split("_")[0]).matches(matches).players(players).build();
	}
}
