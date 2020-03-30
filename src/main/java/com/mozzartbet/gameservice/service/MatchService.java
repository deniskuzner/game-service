package com.mozzartbet.gameservice.service;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.mozzartbet.gameservice.domain.Match;

public interface MatchService {

	void insertSeasonMatches(String season);
	
	List<Match> getBySeason(String season);

	List<Match> getAll();

	Match getById(@NotNull @Min(1) Long id);

	Match insert(Match match);
	
	Match update(Match match);

	int deleteById(@NotNull @Min(1) Long id);
	
	Match getMatch(@NotNull @Min(1) Long matchId);
	
}