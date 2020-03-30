package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;
import com.mozzartbet.gameservice.setup.MatchSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ScoringMapperTest extends BaseMapperTest {

	@Autowired
	TeamMapper teamMapper;

	@Autowired
	MatchMapper matchMapper;

	@Autowired
	ScoringMapper scoringMapper;

	@Autowired
	SeasonMapper seasonMapper;
	
	@Autowired
	MatchSetup matchSetup;

	@Test
	void testCrud() {

		log.info("Insert teams");

		Team t1 = Team.builder().name("Denver Nuggets").url("DEN/2019").build();
		Team t2 = Team.builder().name("Milwaukee Bucks").url("MIL/2019").build();
		assertEquals(teamMapper.insert(t1), 1);
		assertEquals(teamMapper.insert(t2), 1);

		log.info("Insert season");

		Season s1 = Season.builder().year(2019).league("NBA").build();

		assertEquals(seasonMapper.insert(s1), 1);

		log.info("Insert match");
		Match m1 = Match.builder().url("201905160GSW").host(t1).guest(t2).result("111-114").season(s1).build();
		assertEquals(matchMapper.insert(m1), 1);

		List<ScoringQuarter> quarters = Arrays.asList(
				ScoringQuarter.builder().match(m1).quarter("1st Q").hostPoints(10).guestPoints(11).build(),
				ScoringQuarter.builder().match(m1).quarter("2nd Q").hostPoints(15).guestPoints(18).build(),
				ScoringQuarter.builder().match(m1).quarter("3rd Q").hostPoints(14).guestPoints(12).build(),
				ScoringQuarter.builder().match(m1).quarter("4th Q").hostPoints(19).guestPoints(11).build());

		log.info("Insert scoring");

		for (ScoringQuarter scoringQuarter : quarters) {
			assertEquals(scoringMapper.insert(scoringQuarter), 1);
		}

		log.info("Get by id");
		assertEquals(scoringMapper.getById(1L).getMatch().getHost().getId(), t1.getId());
		assertEquals(scoringMapper.getById(1L).getMatch().getGuest().getId(), t2.getId());
		assertEquals(scoringMapper.getById(1L).getQuarter(), "1st Q");
		assertEquals(scoringMapper.getById(1L).getHostPoints(), 10);
		assertEquals(scoringMapper.getById(1L).getGuestPoints(), 11);

		assertEquals(scoringMapper.getById(2L).getMatch().getHost().getId(), t1.getId());
		assertEquals(scoringMapper.getById(2L).getMatch().getGuest().getId(), t2.getId());
		assertEquals(scoringMapper.getById(2L).getQuarter(), "2nd Q");
		assertEquals(scoringMapper.getById(2L).getHostPoints(), 15);
		assertEquals(scoringMapper.getById(2L).getGuestPoints(), 18);
		
		log.info("Get scoring quarters for match");
		List<ScoringQuarter> scoringQuarters = scoringMapper.getForMatch(m1.getId());
		assertEquals(scoringQuarters.size(), 4);
		assertEquals(scoringQuarters.get(0).getHostPoints(), 10);
		assertEquals(scoringQuarters.get(0).getGuestPoints(), 11);
		assertEquals(scoringQuarters.get(1).getHostPoints(), 15);
		assertEquals(scoringQuarters.get(1).getGuestPoints(), 18);
		assertEquals(scoringQuarters.get(2).getHostPoints(), 14);
		assertEquals(scoringQuarters.get(2).getGuestPoints(), 12);
		assertEquals(scoringQuarters.get(3).getHostPoints(), 19);
		assertEquals(scoringQuarters.get(3).getGuestPoints(), 11);

		log.info("Get all");
		assertEquals(scoringMapper.getAll().size(), 4);

		log.info("Update");
		assertEquals(scoringMapper
				.update(ScoringQuarter.builder().id(4L).quarter("1st OT").hostPoints(10).guestPoints(10).build()), 1);
		assertEquals(scoringMapper.getById(4L).getQuarter(), "1st OT");
		assertEquals(scoringMapper.getById(4L).getHostPoints(), 10);
		assertEquals(scoringMapper.getById(4L).getGuestPoints(), 10);

		log.info("Delete");
		assertEquals(scoringMapper.deleteById(4L), 1);
		assertEquals(scoringMapper.getAll().size(), 3);

	}

}