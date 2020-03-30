package com.mozzartbet.gameservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mozzartbet.gameservice.annotation.LogExecutionTime;
import com.mozzartbet.gameservice.dao.TeamDao;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.TeamException;
import com.mozzartbet.gameservice.exception.TeamException.TeamExceptionCode;
import com.mozzartbet.gameservice.service.TeamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class TeamServiceImpl implements TeamService {

	@Autowired
	TeamDao teamDao;

	@Override
	@Transactional(readOnly = true)
	public List<Team> getAll() {
		return teamDao.getAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Team getById(Long id) {
		return teamDao.getById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Team getByUrl(String url) {
		return teamDao.getByUrl(url);
	}

	@LogExecutionTime
	@Override
	public Team save(Team team) {
		try {
			return teamDao.save(team);
		} catch (DuplicateKeyException e) {
			throw new TeamException(TeamExceptionCode.DUPLICATED_URL, "URL: %s is duplicated!", team.getUrl());
		}
	}

	@Override
	public int deleteById(Long id) {
		return teamDao.deleteById(id);
	}

}
