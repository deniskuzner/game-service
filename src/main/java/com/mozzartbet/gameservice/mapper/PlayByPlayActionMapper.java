package com.mozzartbet.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.service.dto.FindPbpActionsRequest;

@Mapper
public interface PlayByPlayActionMapper extends BaseMapper<PlayByPlayAction> {

	public long count();

	public PlayByPlayAction getById(@Param("id") Long id);
	
	public List<PlayByPlayAction> getByMatchId(@Param("id") Long id);
	
	public List<PlayByPlayAction> getAll();

	public int insert(PlayByPlayAction entity);
	
	public int insertSpec(@Param("entity") PlayByPlayAction entity);

	public int update(PlayByPlayAction entity);

	public int deleteById(@Param("id") Long id);
	
	public List<PlayByPlayAction> findPbpActions(@Param("request") FindPbpActionsRequest request, @Param("sent") int sent);
	
	public int updateSent(Long id);
	
	public long countSent(int sent);

}
