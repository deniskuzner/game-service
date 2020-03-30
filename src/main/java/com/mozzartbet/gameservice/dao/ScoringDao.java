package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;

public interface ScoringDao {

public ScoringQuarter getById(Long id);
	
	public List<ScoringQuarter> getAll();

	public ScoringQuarter save(ScoringQuarter scoringQuarter, Match match);

	public int deleteById(Long id);
	
	public List<ScoringQuarter> getForMatch(Long id);
	
	public void deleteForMatch(Long matchId);
}
