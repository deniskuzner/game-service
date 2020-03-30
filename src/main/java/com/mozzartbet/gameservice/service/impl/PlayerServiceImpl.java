package com.mozzartbet.gameservice.service.impl;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mozzartbet.gameservice.dao.PlayerDao;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.exception.PlayerException;
import com.mozzartbet.gameservice.exception.PlayerException.PlayerExceptionCode;
import com.mozzartbet.gameservice.parser.SeasonPlayerPageHtmlParser;
import com.mozzartbet.gameservice.service.PlayerService;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;
import com.mozzartbet.gameservice.service.dto.PlayerSearchResponse;
import com.mozzartbet.gameservice.annotation.LogExecutionTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@LogExecutionTime
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	PlayerDao playerDao;
	
	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	final JmsTemplate jmsTemplate;
	
	@Override
	public void saveSeasonPlayers(String seasonId) {
		List<Player> players = new SeasonPlayerPageHtmlParser(seasonId).parse();
		
		for (Player player : players) {
			save(player);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Player> getAll() {
		return playerDao.getAll();
	}
	
	@Override
	@CacheEvict(cacheNames = "players", key = "#player.id")
	public Player save(Player player) {
		try {
			if(player.getId() == null) {
				playerDao.insert(player);
				return player;
			}
			
			Player existing = (player.getModifiedOn() != null) ? player : playerDao.getById(player.getId());
			if(playerDao.updateOptimistic(player, existing.getModifiedOn()) == 0) {
				throw new OptimisticLockingFailureException("Player id = " + player.getId() + " was changed in meantime!");
			}
			
			return playerDao.getById(player.getId());
		} catch (DuplicateKeyException e) {
			throw new PlayerException(PlayerExceptionCode.DUPLICATED_URL, "PLAYER URL: %s is duplicated!", player.getUrl());
		}
	}

	//Caffeine cached
	@Override
	@Cacheable(cacheNames = "players")
	public Player getById(Long id) {
		return playerDao.getById(id);
	}

	@Override
	public int deleteById(Long id) {
		return playerDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Player> getPlayersForTeam(Long teamId) {
		return playerDao.getPlayersForTeam(teamId);
	}

	@Override
	@Transactional(readOnly = true, isolation = READ_COMMITTED)
	public PlayerSearchResponse searchPlayers(PlayerSearchRequest request) {
		List<Player> players = playerDao.searchPlayers(request); 
		return new PlayerSearchResponse(players);
	}
	
}