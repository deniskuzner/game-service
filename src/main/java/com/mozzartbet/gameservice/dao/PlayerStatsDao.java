package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;

public interface PlayerStatsDao {

	public List<PlayerStats> getAll();

	public PlayerStats getById(Long id);

	public PlayerStats getByMatchId(Long matchId, Long playerId);
	
	public List<PlayerStats> getStatsForMatch(Long matchId);

	public int deleteById(Long id);
	
	public PlayerStats save(PlayerStats playerStats, Match match);
	
	public void deleteForMatch(Long matchId);

}
