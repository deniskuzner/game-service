package com.mozzartbet.gameservice.service.impl;

import java.util.List;

import static java.util.concurrent.TimeUnit.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.mozzartbet.gameservice.annotation.LogExecutionTime;
import com.mozzartbet.gameservice.dao.MatchDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.exception.MatchException;
import com.mozzartbet.gameservice.exception.MatchException.MatchExceptionCode;
import com.mozzartbet.gameservice.parser.SeasonMatchPageHtmlParser;
import com.mozzartbet.gameservice.service.MatchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@LogExecutionTime
public class MatchServiceImpl implements MatchService {

	@Autowired
	MatchDao matchDao;

	@Override
	public void insertSeasonMatches(String season) {
		List<Match> seasonMatches = new SeasonMatchPageHtmlParser(season).parse();
		for (Match match : seasonMatches) {
			insert(match);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Match> getAll() {
		return matchDao.getAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Match> getBySeason(String season) {
		return matchDao.getBySeason(season);
	}

	@Override
	@Transactional(readOnly = true)
	public Match getById(Long id) {
		return matchDao.getById(id);
	}

	@Override
	public Match insert(Match match) {
		try {
			return matchDao.insert(match);
		} catch (DuplicateKeyException e) {
			throw new MatchException(MatchExceptionCode.DUPLICATED_URL, "URL: %s is duplicated!", match.getUrl());
		}
	}

	@Override
	public Match update(Match match) {
		matchDao.update(match);
		return matchDao.getById(match.getId());
	}

	@Override
	public int deleteById(Long id) {
		return matchDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Match getMatch(Long matchId) {
		return matchCache.getUnchecked(matchId);
	}
	
	final LoadingCache<Long, Match> matchCache = CacheBuilder.newBuilder()
			.initialCapacity(100)
			.maximumSize(1000)
			.expireAfterWrite(30, SECONDS)
			.recordStats()
			.build(new CacheLoader<Long, Match>() {
				@Override
				public Match load(Long id) throws Exception {
					return matchDao.getById(id);
				}
				
			});
	
	@VisibleForTesting
	public CacheStats matchCacheStats() {
		return matchCache.stats();
	}
}
