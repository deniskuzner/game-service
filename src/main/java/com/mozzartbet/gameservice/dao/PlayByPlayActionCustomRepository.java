package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.dto.TeamQuarterPoints;

public interface PlayByPlayActionCustomRepository {
	
	public List<TeamQuarterPoints> getQuarterPoints(Long matchId);
	
	public List<PlayerStats> getPlayerStats(Long matchId);

}
