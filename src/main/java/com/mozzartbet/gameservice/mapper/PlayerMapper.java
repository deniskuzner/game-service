package com.mozzartbet.gameservice.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;

@Mapper
public interface PlayerMapper extends BaseMapper<Player> {

	public long count();
	
	public List<Player> getAll();

	public Player getById(@Param("id") Long id);
	
	public Player getByUrl(@Param("url") String url, @Param("teamId") Long teamId);

	public int insert(Player entity);

	public int update(Player entity);

	public int deleteById(@Param("id") Long id);

	public List<Player> getPlayersForTeam(@Param("teamId") Long teamId);
	
	public List<Player> searchPlayers(@Param("request") PlayerSearchRequest request);
	
	public int updateOptimistic(@Param("player") Player player, @Param("expectedModifiedOn") LocalDateTime expectedModifiedOn);

}
