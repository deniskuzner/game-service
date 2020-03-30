package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.statistics.PlayerStats;

@Mapper
public interface PlayerStatsMapper extends BaseMapper<PlayerStats>{

	public long count();

	public List<PlayerStats> getAll();
	
	public PlayerStats getById(@Param("id") Long id);

	public PlayerStats getByMatchId(@Param("matchId") Long matchId, @Param("playerId") Long playerId);

	public int insert(PlayerStats entity);

	public int update(PlayerStats entity);

	public int deleteById(@Param("id") Long id);
	
	public List<PlayerStats> getStatsForMatch(@Param("matchId") Long matchId);
	
	public void deleteForMatch(@Param("matchId") Long matchId);
	
}
