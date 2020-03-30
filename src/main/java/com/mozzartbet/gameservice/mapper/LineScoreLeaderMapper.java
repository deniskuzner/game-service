package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.statistics.linescore.LineScoreLeader;

@Mapper
public interface LineScoreLeaderMapper extends BaseMapper<LineScoreLeader> {

	public long count();

	public List<LineScoreLeader> getAll();

	public LineScoreLeader getById(@Param("id") Long id);

	public List<LineScoreLeader> getAllForMatch(@Param("matchId") Long matchId);

	public List<LineScoreLeader> getForMatchByType(@Param("matchId") Long matchId, @Param("type") String type);

	public int insert(@Param("entity") LineScoreLeader entity);

	public int update(@Param("entity") LineScoreLeader entity);

	public int deleteById(@Param("id") Long id);
	
	public int deleteForMatch(@Param("matchId") Long matchId);

}
