package com.mozzartbet.gameservice.stats;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.LineScore;
import com.mozzartbet.gameservice.domain.statistics.MatchStats;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;

public class MatchStatsCalculator {
	private Match match;

	public MatchStatsCalculator(Match match) {
		this.match = match;
	}

	public MatchStats getMatchStats() {
		List<PlayerStats> playerStats = new PlayerStatsCalculator(match).getPlayerStats();
		List<TeamStats> teamStats = new TeamStatsCalculator(match, playerStats).getTeamStats();
		LineScore lineScore = new LineScoreCalculator(match, playerStats).getLineScore();
		
		return MatchStats.builder().match(match).playerStats(playerStats).teamStats(teamStats).lineScore(lineScore).build();
	}

}
