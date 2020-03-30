package com.mozzartbet.gameservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mozzartbet.gameservice.annotation.LogExecutionTime;
import com.mozzartbet.gameservice.dao.LineScoreLeaderDao;
import com.mozzartbet.gameservice.dao.MatchDao;
import com.mozzartbet.gameservice.dao.PlayerStatsDao;
import com.mozzartbet.gameservice.dao.ScoringDao;
import com.mozzartbet.gameservice.dao.TeamStatsDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.LineScore;
import com.mozzartbet.gameservice.domain.statistics.MatchStats;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.AssistLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.LineScoreLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ReboundingLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;
import com.mozzartbet.gameservice.exception.MatchStatsException;
import com.mozzartbet.gameservice.exception.MatchStatsException.MatchStatsExceptionCode;
import com.mozzartbet.gameservice.service.MatchStatsService;
import com.mozzartbet.gameservice.stats.MatchStatsCalculator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class MatchStatsServiceImpl implements MatchStatsService {

	@Autowired
	PlayerStatsDao playerStatsDao;

	@Autowired
	TeamStatsDao teamStatsDao;

	@Autowired
	ScoringDao scoringDao;

	@Autowired
	LineScoreLeaderDao lineScoreLeaderDao;

	@Autowired
	MatchDao matchDao;

	@Override
	@LogExecutionTime
	public void insertSeasonMatchStats(String season) {
		List<Match> seasonMatches = matchDao.getBySeason(season);

		for (Match match : seasonMatches) {
			insert(match);
		}
	}

	@Override
	public MatchStats insert(Match match) {
		if (match.getId() == null)
			match.setId(matchDao.getByUrl(match.getUrl()).getId());

		MatchStats matchStats = new MatchStatsCalculator(match).getMatchStats();

		try {

			for (PlayerStats playerStats : matchStats.getPlayerStats()) {
				playerStatsDao.save(playerStats, match);
			}

			for (TeamStats teamStats : matchStats.getTeamStats()) {
				teamStatsDao.save(teamStats, match);
			}

			for (ScoringQuarter scoringQuarter : matchStats.getLineScore().getScoringQuarters()) {
				scoringDao.save(scoringQuarter, match);
			}

			for (ScoringLeader scoringLeader : matchStats.getLineScore().getScoringLeaders()) {
				lineScoreLeaderDao.insert(scoringLeader, match);
			}

			for (AssistLeader assistLeader : matchStats.getLineScore().getAssistLeaders()) {
				lineScoreLeaderDao.insert(assistLeader, match);
			}

			for (ReboundingLeader reboundingLeader : matchStats.getLineScore().getReboundingLeaders()) {
				lineScoreLeaderDao.insert(reboundingLeader, match);
			}

			return getByMatchId(match.getId());
		} catch (DuplicateKeyException e) {
			throw new MatchStatsException(MatchStatsExceptionCode.DUPLICATED_MATCH_STATS, "Duplicated stats for match: %s", match.getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public MatchStats getByMatchId(Long matchId) {

		Match match = matchDao.getById(matchId);
		List<PlayerStats> playersStats = playerStatsDao.getStatsForMatch(matchId);
		List<TeamStats> teamsStats = teamStatsDao.getForMatch(matchId);
		LineScore lineScore = LineScore.builder().matchUrl(match.getUrl())
				.scoringQuarters(scoringDao.getForMatch(matchId)).scoringLeaders(lineScoreLeaderDao.getForMatchByType(ScoringLeader.class, matchId, "scoring_leader"))
				.assistLeaders(lineScoreLeaderDao.getForMatchByType(AssistLeader.class, matchId, "assist_leader"))
				.reboundingLeaders(lineScoreLeaderDao.getForMatchByType(ReboundingLeader.class, matchId, "rebounding_leader")).build();
		return MatchStats.builder().match(match).playerStats(playersStats).teamStats(teamsStats).lineScore(lineScore).build();
	}

	@Override
	public void deleteByMatchId(Long matchId) {
		playerStatsDao.deleteForMatch(matchId);
		teamStatsDao.deleteForMatch(matchId);
		scoringDao.deleteForMatch(matchId);
		lineScoreLeaderDao.deleteForMatch(matchId);
	}

	@Override
	public PlayerStats savePlayerStats(PlayerStats playerStats, Match match) {
		return playerStatsDao.save(playerStats, match);
	}

	@Override
	public TeamStats saveTeamStats(TeamStats teamStats, Match match) {
		return teamStatsDao.save(teamStats, match);
	}

	@Override
	public ScoringQuarter save(ScoringQuarter scoringQuarter, Match match) {
		return scoringDao.save(scoringQuarter, match);
	}

	@Override
	public int updateLineScoreLeader(LineScoreLeader lineScoreLeader) {
		return lineScoreLeaderDao.update(lineScoreLeader);
	}

}
