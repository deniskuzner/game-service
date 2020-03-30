package com.mozzartbet.gameservice.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.PlayerDao;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.mapper.PlayerMapper;
import com.mozzartbet.gameservice.mapper.TeamMapper;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;

@Repository
public class PlayerDaoImpl implements PlayerDao {

	@Autowired
	PlayerMapper playerMapper;

	@Autowired
	TeamMapper teamMapper;

	@Override
	public List<Player> getAll() {
		return playerMapper.getAll();
	}

	@Override
	public Player getById(Long id) {
		return playerMapper.getById(id);
	}

	@Override
	public int deleteById(Long id) {
		return playerMapper.deleteById(id);
	}

	@Override
	public Player save(Player player) {
		if (teamMapper.getByUrl(player.getTeam().getUrl()) == null)
			teamMapper.insert(player.getTeam());

		player.setTeam(teamMapper.getByUrl(player.getTeam().getUrl()));
		return playerMapper.save(player);
	}

	@Override
	public Player getByUrl(String url, Long teamId) {
		return playerMapper.getByUrl(url, teamId);
	}

	@Override
	public List<Player> getPlayersForTeam(Long teamId) {
		return playerMapper.getPlayersForTeam(teamId);
	}

	@Override
	public List<Player> searchPlayers(PlayerSearchRequest request) {
		return playerMapper.searchPlayers(request);
	}

	@Override
	public int updateOptimistic(Player player, LocalDateTime expectedModifiedOn) {
		return playerMapper.updateOptimistic(player, expectedModifiedOn);
	}

	@Override
	public int insert(Player player) {
		if (teamMapper.getByUrl(player.getTeam().getUrl()) == null)
			teamMapper.insert(player.getTeam());

		player.setTeam(teamMapper.getByUrl(player.getTeam().getUrl()));
		return playerMapper.insert(player);
	}

}
