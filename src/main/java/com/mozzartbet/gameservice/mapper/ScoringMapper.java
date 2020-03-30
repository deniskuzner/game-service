package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;

@Mapper
public interface ScoringMapper extends BaseMapper<ScoringQuarter>{

	public long count();

	public ScoringQuarter getById(@Param("id") Long id);
	
	public List<ScoringQuarter> getForMatch(@Param("id") Long id);
	
	public List<ScoringQuarter> getAll();

	public int insert(ScoringQuarter entity);
	
	public int update(ScoringQuarter entity);

	public int deleteById(@Param("id") Long id);
	
	public int deleteForMatch(@Param("matchId") Long matchId);
	
}
