package com.mozzartbet.gameservice.stats;

import java.util.ArrayList;
import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.MatchStats;

public class SeasonStatsCalculator {
	
	public List<MatchStats> getSeasonStats(List<Match> seasonMatches){
		List<MatchStats> seasonStats = new ArrayList<>();
		
		for (Match match : seasonMatches) {
			seasonStats.add(new MatchStatsCalculator(match).getMatchStats());
		}
		
		return seasonStats;
	}
}
