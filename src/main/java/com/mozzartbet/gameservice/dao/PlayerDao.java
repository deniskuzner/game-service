package com.mozzartbet.gameservice.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;

public interface PlayerDao {

	List<Player> getAll();

	Player getById(Long id);
	
	Player getByUrl(String url, Long teamId);

	Player save(Player player);
	
	int insert(Player entity);
	
	int deleteById(Long id);
	
	List<Player> getPlayersForTeam(Long teamId);

	List<Player> searchPlayers(PlayerSearchRequest request);
	
	int updateOptimistic(Player player, LocalDateTime expectedModifiedOn);
	
}
