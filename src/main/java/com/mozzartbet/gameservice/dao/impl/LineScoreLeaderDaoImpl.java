package com.mozzartbet.gameservice.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.LineScoreLeaderDao;
import com.mozzartbet.gameservice.dao.PlayerDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.linescore.AssistLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.LineScoreLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ReboundingLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringLeader;
import com.mozzartbet.gameservice.mapper.LineScoreLeaderMapper;
import com.mozzartbet.gameservice.mapper.PlayerMapper;

@Repository
public class LineScoreLeaderDaoImpl implements LineScoreLeaderDao {

	@Autowired
	LineScoreLeaderMapper lineScoreLeaderMapper;
	
	@Autowired
	PlayerMapper playerMapper;
	
	@Autowired
	PlayerDao playerDao;
	
	@Override
	public List<LineScoreLeader> getAll() {
		return lineScoreLeaderMapper.getAll();
	}

	@Override
	public LineScoreLeader getById(Long id) {
		return lineScoreLeaderMapper.getById(id);
	}

	@Override
	public List<LineScoreLeader> getAllForMatch(Long matchId) {
		return lineScoreLeaderMapper.getAllForMatch(matchId);
	}
	
	@Override
	public <T extends LineScoreLeader> List<T> getForMatchByType(Class<T> subclass, Long matchId, String type) {
		List<LineScoreLeader> lineScoreLeaders = lineScoreLeaderMapper.getForMatchByType(matchId, type);
		List<T> resultList = lineScoreLeaders.stream().map(leader -> (T) leader).collect(Collectors.toList());
		return resultList;
	}

	@Override
	public int insert(LineScoreLeader lineScoreLeader, Match match) {
		
		if(lineScoreLeader instanceof ScoringLeader) {
			ScoringLeader scoringLeader = (ScoringLeader) lineScoreLeader;
			scoringLeader.setMatchId(match.getId());
			scoringLeader.setPlayer(playerMapper.getByUrl(scoringLeader.getPlayer().getUrl(), scoringLeader.getPlayer().getTeam().getId()));
			return lineScoreLeaderMapper.insert(scoringLeader);
		}
		
		if(lineScoreLeader instanceof AssistLeader) {
			AssistLeader assistLeader = (AssistLeader) lineScoreLeader;
			assistLeader.setMatchId(match.getId());
			assistLeader.setPlayer(playerMapper.getByUrl(assistLeader.getPlayer().getUrl(), assistLeader.getPlayer().getTeam().getId()));
			return lineScoreLeaderMapper.insert(assistLeader);
		}
		
		if(lineScoreLeader instanceof ReboundingLeader) {
			ReboundingLeader reboundingLeader = (ReboundingLeader) lineScoreLeader;
			reboundingLeader.setMatchId(match.getId());
			reboundingLeader.setPlayer(playerMapper.getByUrl(reboundingLeader.getPlayer().getUrl(), reboundingLeader.getPlayer().getTeam().getId()));
			return lineScoreLeaderMapper.insert(reboundingLeader);
		}
		
		return 0;
	}
	
	@Override
	public int update(LineScoreLeader lineScoreLeader) {
		return lineScoreLeaderMapper.update(lineScoreLeader);
	}

	@Override
	public int deleteById(Long id) {
		return lineScoreLeaderMapper.deleteById(id);
	}

	@Override
	public void deleteForMatch(Long matchId) {
		lineScoreLeaderMapper.deleteForMatch(matchId);
	}

}
