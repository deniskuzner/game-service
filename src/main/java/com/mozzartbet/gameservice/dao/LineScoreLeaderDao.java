package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.linescore.LineScoreLeader;

public interface LineScoreLeaderDao {

	public List<LineScoreLeader> getAll();

	public LineScoreLeader getById(Long id);

	public List<LineScoreLeader> getAllForMatch(Long matchId);
	
	public <T extends LineScoreLeader> List<T> getForMatchByType(Class<T> subclass, Long matchId, String type);
	
	public int insert(LineScoreLeader lineScoreLeader, Match match);
	
	public int update(LineScoreLeader lineScoreLeader);

	public int deleteById(Long id);
	
	public void deleteForMatch(Long matchId);
	
}
