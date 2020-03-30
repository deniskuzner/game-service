package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.Team;

@Mapper
public interface TeamMapper extends BaseMapper<Team> {

	public long count();
	
	public List<Team> getAll();

	public Team getById(@Param("id") Long id);
	
	public Team getByUrl(@Param("url") String url);

	public int insert(Team entity);

	public int update(Team entity);

	public int deleteById(@Param("id") Long id);

}