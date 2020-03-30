package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.ScoringDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;
import com.mozzartbet.gameservice.mapper.ScoringMapper;

@Repository
public class ScoringDaoImpl implements ScoringDao {
	
	@Autowired
	ScoringMapper scoringMapper;

	@Override
	public ScoringQuarter getById(Long id) {
		return scoringMapper.getById(id);
	}

	@Override
	public List<ScoringQuarter> getAll() {
		return scoringMapper.getAll();
	}

	@Override
	public ScoringQuarter save(ScoringQuarter scoringQuarter, Match match) {
		scoringQuarter.setMatch(match);
		return scoringMapper.save(scoringQuarter);
	}

	@Override
	public int deleteById(Long id) {
		return scoringMapper.deleteById(id);
	}

	@Override
	public List<ScoringQuarter> getForMatch(Long id) {
		return scoringMapper.getForMatch(id);
	}

	@Override
	public void deleteForMatch(Long matchId) {
		scoringMapper.deleteForMatch(matchId);
	}

}
