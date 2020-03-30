package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.Team;

public interface TeamDao {

	public long count();
	
	public List<Team> getAll();

	public Team getById(Long id);
	
	public Team getByUrl(String url);
	
	public Team save(Team team);

	public int deleteById(Long id);
}
