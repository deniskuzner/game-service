package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.TeamDao;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.mapper.TeamMapper;

@Repository
public class TeamDaoImpl implements TeamDao {
	
	@Autowired
	TeamMapper teamMapper;

	@Override
	public long count() {
		return teamMapper.count();
	}

	@Override
	public List<Team> getAll() {
		return teamMapper.getAll();
	}

	@Override
	public Team getById(Long id) {
		return teamMapper.getById(id);
	}

	@Override
	public Team getByUrl(String url) {
		return teamMapper.getByUrl(url);
	}
	
	@Override
	public Team save(Team team) {
		return teamMapper.save(team);
	}

	@Override
	public int deleteById(Long id) {
		return teamMapper.deleteById(id);
	}

}
