package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.Season;

@Mapper
public interface SeasonMapper extends BaseMapper<Season> {
	
	public long count();

	public Season getById(@Param("id") Long id);
	
	public List<Season> getAll();
	
	public Season getByYear(@Param("year") int year, @Param("league") String league);

	public int insert(Season entity);
	
	public int update(Season entity);

	public int deleteById(@Param("id") Long id);

}
