package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.PlayerDao;
import com.mozzartbet.gameservice.dao.PlayerStatsDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.mapper.PlayerStatsMapper;
import com.mozzartbet.gameservice.mapper.TeamMapper;

@Repository
public class PlayerStatsDaoImpl implements PlayerStatsDao {

	@Autowired
	PlayerStatsMapper playerStatsMapper;

	@Autowired
	PlayerDao playerDao;

	@Autowired
	TeamMapper teamMapper;

	@Override
	public List<PlayerStats> getAll() {
		return playerStatsMapper.getAll();
	}

	@Override
	public PlayerStats getById(Long id) {
		return playerStatsMapper.getById(id);
	}

	@Override
	public PlayerStats getByMatchId(Long matchId, Long playerId) {
		return playerStatsMapper.getByMatchId(matchId, playerId);
	}

	@Override
	public List<PlayerStats> getStatsForMatch(Long matchId) {
		return playerStatsMapper.getStatsForMatch(matchId);
	}

	@Override
	public int deleteById(Long id) {
		return playerStatsMapper.deleteById(id);
	}

	@Override
	public PlayerStats save(PlayerStats playerStats, Match match) {
		
		Player player = playerDao.getByUrl(playerStats.getPlayer().getUrl(), teamMapper.getByUrl(playerStats.getPlayer().getTeam().getUrl()).getId());
		
		if(player == null) {
			playerStats.setPlayer(playerDao.save(playerStats.getPlayer()));
		} else { 
			playerStats.setPlayer(player);
		}
		
		playerStats.setMatchId(match.getId());

		return playerStatsMapper.save(playerStats);
	}

	@Override
	public void deleteForMatch(Long matchId) {
		playerStatsMapper.deleteForMatch(matchId);
	}

}
