package com.mozzartbet.gameservice.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.MatchStats;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.LineScoreLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;

public interface MatchStatsService {

	void insertSeasonMatchStats(String season);

	MatchStats insert(Match match);

	MatchStats getByMatchId(@NotNull @Min(1) Long matchId);

	void deleteByMatchId(@NotNull @Min(1) Long matchId);

	PlayerStats savePlayerStats(PlayerStats playerStats, Match match);

	TeamStats saveTeamStats(TeamStats teamStats, Match match);

	ScoringQuarter save(ScoringQuarter scoringQuarter, Match match);

	int updateLineScoreLeader(LineScoreLeader lineScoreLeader);

}
