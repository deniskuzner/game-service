package com.mozzartbet.gameservice.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;
import com.mozzartbet.gameservice.service.dto.PlayerSearchResponse;

public interface PlayerService {

	void saveSeasonPlayers(String seasonId);

	List<Player> getAll();
	
	List<Player> getPlayersForTeam(@NotNull @Min(1) Long teamId);

	Player getById(@NotNull @Min(1) Long id);

	Player save(Player player);
	
	int deleteById(@NotNull @Min(1) Long id);
	
	@Valid PlayerSearchResponse searchPlayers(@Valid PlayerSearchRequest request);

}
