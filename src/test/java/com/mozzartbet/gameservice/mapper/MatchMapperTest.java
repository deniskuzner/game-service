package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.service.MatchService;
import com.mozzartbet.gameservice.service.PlayerService;
import com.mozzartbet.gameservice.service.TeamService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MatchMapperTest extends BaseMapperTest {
	
	@Autowired
	MatchMapper matchMapper;
	
	@Autowired
	TeamMapper teamMapper;
	
	@Autowired
	PlayerMapper playerMapper;
	
	@Autowired
	SeasonMapper seasonMapper;
	
	@Autowired
	MatchService matchService;
	
	@Autowired
	PlayerService playerService;
	
	@Autowired
	TeamService teamService;
	
	@Test
	void testCrud() {
		assertEquals(matchMapper.count(), 0L);
		
		log.info("Adding a new team");
		Team t1 = Team.builder().name("Denver Nuggets").url("DEN/2019").build();
		Team t2 = Team.builder().name("Milwaukee Bucks").url("MIL/2019").build();
		assertEquals(teamMapper.insert(t1), 1);
		assertEquals(teamMapper.insert(t2), 1);
		
		log.info("Insert season");
		Season s = Season.builder().year(2019).league("NBA").build();
		assertEquals(seasonMapper.insert(s), 1);
		
		log.info("Insert match");
		Match m1 = Match.builder().url("201905160GSW").host(t1).guest(t2).result("111-114").season(s).build();
		assertEquals(matchMapper.insert(m1), 1);
		
		log.info("Get match by id");
		
		Match matchById = matchMapper.getById(m1.getId());
		
		assertEquals(matchById.getId(), m1.getId());
		assertEquals(matchById.getUrl(), m1.getUrl());
		assertEquals(matchById.getResult(), m1.getResult());
		assertEquals(matchById.getHost().getId(), t1.getId());
		assertEquals(matchById.getHost().getName(), t1.getName());
		assertEquals(matchById.getHost().getUrl(), t1.getUrl());
		assertEquals(matchById.getGuest().getId(), t2.getId());
		assertEquals(matchById.getGuest().getName(), t2.getName());
		assertEquals(matchById.getGuest().getUrl(), t2.getUrl());
		assertEquals(matchById.getSeason().getId(), s.getId());
		assertEquals(matchById.getSeason().getYear(), 2019);
		assertEquals(matchById.getSeason().getLeague(), "NBA");
		
		log.info("Get all");
		matchMapper.insert(Match.builder().url("201810160BOS").host(t1).guest(t2).result("114-100").season(s).build());
		List<Match> allMatches = matchMapper.getAll();
		assertEquals(allMatches.size(), 2);
		
		log.info("Update match");
		assertEquals(matchMapper.update(Match.builder().id(m1.getId()).url("updated url").host(t1).guest(t2).result("132-115").build()) , 1);
		Match updatedMatch = matchMapper.getById(m1.getId());
		assertEquals(updatedMatch.getId(), m1.getId());
		assertEquals(updatedMatch.getUrl(), "updated url");
		assertEquals(updatedMatch.getResult(), "132-115");
		assertEquals(updatedMatch.getHost().getId(), t1.getId());
		assertEquals(updatedMatch.getHost().getName(), t1.getName());
		assertEquals(updatedMatch.getHost().getUrl(), t1.getUrl());
		assertEquals(updatedMatch.getGuest().getId(), t2.getId());
		assertEquals(updatedMatch.getGuest().getName(), t2.getName());
		assertEquals(updatedMatch.getGuest().getUrl(), t2.getUrl());
		
		log.info("Delete match");
		assertEquals(matchMapper.deleteById(m1.getId()), 1);
		assertEquals(matchMapper.getAll().size(), 1);
	}
	
}