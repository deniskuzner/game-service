package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.statistics.TeamStats;

@Mapper
public interface TeamStatsMapper extends BaseMapper<TeamStats> {

	public long count();

	public List<TeamStats> getAll();
	
	public TeamStats getById(@Param("id") Long id);

	public int insert(TeamStats entity);

	public int update(TeamStats entity);

	public int deleteById(@Param("id") Long id);
	
	public List<TeamStats> getForMatch(@Param("matchId") Long matchId);
	
	public void deleteForMatch(@Param("matchId") Long matchId);
	
}
