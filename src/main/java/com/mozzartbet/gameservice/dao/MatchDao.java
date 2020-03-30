package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;

public interface MatchDao {

	public Match getById(Long id);
	
	public Match getByUrl(String url);
	
	public List<Match> getAll();

	public List<Match> getBySeason(String season);

	public Match insert(Match match);
	
	public int update(Match match);

	public int deleteById(Long id);
	
}