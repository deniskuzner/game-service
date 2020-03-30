package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.Match;

@Mapper
public interface MatchMapper extends BaseMapper<Match> {
	
	public long count();

	public Match getById(@Param("id") Long id);
	
	public Match getByUrl(@Param("url") String url);
	
	public List<Match> getBySeason(@Param("seasonId") Long seasonId);
	
	public List<Match> getAll();

	public int insert(Match entity);
	
	public int update(Match entity);

	public int deleteById(@Param("id") Long id);
	
}
