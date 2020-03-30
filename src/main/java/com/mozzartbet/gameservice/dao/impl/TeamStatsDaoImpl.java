package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.TeamStatsDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;
import com.mozzartbet.gameservice.mapper.TeamStatsMapper;

@Repository
public class TeamStatsDaoImpl implements TeamStatsDao {

	@Autowired
	TeamStatsMapper teamStatsMapper;
	
	@Override
	public List<TeamStats> getAll() {
		return teamStatsMapper.getAll();
	}

	@Override
	public TeamStats getById(Long id) {
		return teamStatsMapper.getById(id);
	}

	@Override
	public int deleteById(Long id) {
		return teamStatsMapper.deleteById(id);
	}

	@Override
	public List<TeamStats> getForMatch(Long matchId) {
		return teamStatsMapper.getForMatch(matchId);
	}

	@Override
	public TeamStats save(TeamStats teamStats, Match match) {
		teamStats.setMatchId(match.getId());
		return teamStatsMapper.save(teamStats);
	}

	@Override
	public void deleteForMatch(Long matchId) {
		teamStatsMapper.deleteForMatch(matchId);
	}
	
}
