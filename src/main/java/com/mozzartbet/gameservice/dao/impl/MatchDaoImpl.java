package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.MatchDao;
import com.mozzartbet.gameservice.dao.PlayByPlayActionRepository;
import com.mozzartbet.gameservice.dao.PlayerDao;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;
import com.mozzartbet.gameservice.mapper.MatchMapper;
import com.mozzartbet.gameservice.mapper.PlayByPlayActionMapper;
import com.mozzartbet.gameservice.mapper.PlayerMapper;
import com.mozzartbet.gameservice.mapper.SeasonMapper;
import com.mozzartbet.gameservice.mapper.TeamMapper;

@Repository
public class MatchDaoImpl implements MatchDao {

	@Autowired
	MatchMapper matchMapper;

	@Autowired
	SeasonMapper seasonMapper;

	@Autowired
	TeamMapper teamMapper;

	@Autowired
	PlayerMapper playerMapper;

	@Autowired
	PlayByPlayActionMapper playByPlayActionMapper;

	@Autowired
	PlayerDao playerDao;
	
	@Autowired
	PlayByPlayActionRepository playByPlayActionRepository;

	@Override
	public Match getById(Long id) {
		Match match = matchMapper.getById(id);
		match.setPbpActions(playByPlayActionMapper.getByMatchId(id));
		return match;
	}

	@Override
	public Match getByUrl(String url) {
		Match match = matchMapper.getByUrl(url);
		match.setPbpActions(playByPlayActionMapper.getByMatchId(match.getId()));
		return match;
	}

	@Override
	public List<Match> getBySeason(String season) {
		Long seasonId = seasonMapper.getByYear(Integer.parseInt(season.substring(4, 8)), season.substring(0, 3)).getId();
		List<Match> matches = matchMapper.getBySeason(seasonId);
		for (Match match : matches) {
			match.setPbpActions(playByPlayActionMapper.getByMatchId(match.getId()));
		}
		return matches;
	}

	@Override
	public List<Match> getAll() {
		List<Match> matches = matchMapper.getAll();
		for (Match match : matches) {
			match.setPbpActions(playByPlayActionMapper.getByMatchId(match.getId()));
		}
		return matches;
	}

	@Override
	public Match insert(Match match) {
		match.setHost(teamMapper.getByUrl(match.getHost().getUrl()));
		match.setGuest(teamMapper.getByUrl(match.getGuest().getUrl()));
		match.setSeason(seasonMapper.getByYear(match.getSeason().getYear(), match.getSeason().getLeague()));

		matchMapper.insert(match);
		
		for (PlayByPlayAction action : match.getPbpActions()) {

			action.setMatchId(match.getId());
			action.setTeam(teamMapper.getByUrl(action.getTeam().getUrl()));
			playByPlayActionMapper.insert(action);

			Long actionTeamId = action.getTeam().getId();

			if (action instanceof Foul) {
				Foul foul = (Foul) action;

				if (!foul.getFoulByPlayer().getUrl().isEmpty())
					foul.setFoulByPlayer(playerDao.getByUrl(foul.getFoulByPlayer().getUrl(), actionTeamId));

				playByPlayActionMapper.insertSpec(foul);
				playByPlayActionRepository.insert(foul);
				continue;
			}
			if (action instanceof Miss) {
				Miss miss = (Miss) action;

				if (!miss.getMissPlayer().getUrl().isEmpty())
					miss.setMissPlayer(playerDao.getByUrl(miss.getMissPlayer().getUrl(), actionTeamId));
				if (!miss.getBlockPlayer().getUrl().isEmpty())
					miss.setBlockPlayer(playerDao.getByUrl(miss.getBlockPlayer().getUrl(), actionTeamId == match.getHost().getId() ? match.getGuest().getId() : actionTeamId));

				playByPlayActionMapper.insertSpec(miss);
				playByPlayActionRepository.insert(miss);
				continue;
			}
			if (action instanceof Point) {
				Point point = (Point) action;

				if (!point.getPointPlayer().getUrl().isEmpty())
					point.setPointPlayer(playerDao.getByUrl(point.getPointPlayer().getUrl(), actionTeamId));
				if (!point.getAssistPlayer().getUrl().isEmpty())
					point.setAssistPlayer(playerDao.getByUrl(point.getAssistPlayer().getUrl(), actionTeamId));

				playByPlayActionMapper.insertSpec(point);
				playByPlayActionRepository.insert(point);
				continue;
			}
			if (action instanceof Rebound) {
				Rebound rebound = (Rebound) action;
				rebound.setPlayer(playerDao.getByUrl(rebound.getPlayer().getUrl(), actionTeamId));
				playByPlayActionMapper.insertSpec(rebound);
				playByPlayActionRepository.insert(rebound);
				continue;
			}
			if (action instanceof Substitution) {
				Substitution substitution = (Substitution) action;

				if (!substitution.getInPlayer().getUrl().isEmpty())
					substitution.setInPlayer(playerDao.getByUrl(substitution.getInPlayer().getUrl(), actionTeamId));
				if (!substitution.getOutPlayer().getUrl().isEmpty())
					substitution.setOutPlayer(playerDao.getByUrl(substitution.getOutPlayer().getUrl(), actionTeamId));

				playByPlayActionMapper.insertSpec(substitution);
				playByPlayActionRepository.insert(substitution);
				continue;

			}
			if (action instanceof Turnover) {
				Turnover turnover = (Turnover) action;

				if (!turnover.getTurnoverPlayer().getUrl().isEmpty())
					turnover.setTurnoverPlayer(playerDao.getByUrl(turnover.getTurnoverPlayer().getUrl(), actionTeamId));
				if (!turnover.getStealPlayer().getUrl().isEmpty())
					turnover.setStealPlayer(playerDao.getByUrl(turnover.getStealPlayer().getUrl(), actionTeamId == match.getHost().getId() ? match.getGuest().getId() : actionTeamId));

				playByPlayActionMapper.insertSpec(turnover);
				playByPlayActionRepository.insert(turnover);
				continue;
			}

		}

		match = matchMapper.getById(match.getId());
		match.setPbpActions(playByPlayActionMapper.getByMatchId(match.getId()));
		return match;
	}

	@Override
	public int update(Match match) {
		return matchMapper.update(match);
	}

	@Override
	public int deleteById(Long id) {
		return matchMapper.deleteById(id);
	}

}
