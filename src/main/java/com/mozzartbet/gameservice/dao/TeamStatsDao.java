package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;

public interface TeamStatsDao {

	public List<TeamStats> getAll();
	
	public TeamStats getById(Long id);

	public int deleteById(Long id);
	
	public List<TeamStats> getForMatch(Long matchId);
	
	public TeamStats save(TeamStats teamStats, Match match);
	
	public void deleteForMatch(Long matchId);
	
}