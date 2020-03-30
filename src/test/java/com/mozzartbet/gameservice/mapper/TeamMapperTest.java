package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Team;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TeamMapperTest extends BaseMapperTest {

	@Autowired
	private TeamMapper teamMapper;

	@Test
	public void testCrud() throws Exception {
		assertEquals(teamMapper.count(), 0L);

		LocalDateTime now = LocalDateTime.now();

		log.info("Adding a new team");
		Team t = Team.builder().name("Denver Nuggets").url("dn_url_id").build();
		assertEquals(teamMapper.insert(t), 1);

		Long id = t.getId();
		assertNotNull(id);

		log.info("Reloading a team");
		t = teamMapper.getById(id);
		assertEquals(t.getId(), id);
		assertEquals(t.getName(), "Denver Nuggets");
		assertEquals(t.getUrl(), "dn_url_id");
		assertTrue(t.getCreatedOn().isAfter(now));
		assertEquals(t.getCreatedOn(), t.getModifiedOn());

		log.info("Updating a team");
		Team team = Team.builder().url("gsw_url_id").name("Golden State Warriors").id(id).build();
		assertEquals(teamMapper.update(team), 1);
		team = teamMapper.getById(id);
		assertEquals(team.getId(), id);
		assertEquals(team.getName(), "Golden State Warriors");
		assertEquals(team.getUrl(), "gsw_url_id");
		assertTrue(team.getCreatedOn().isAfter(now));
		assertEquals(team.getCreatedOn(), t.getModifiedOn());

		log.info("Deleting a team");
		assertEquals(teamMapper.deleteById(id), 1);
		assertEquals(teamMapper.getById(id), null);

		log.info("Getting all teams");
		Team t1 = Team.builder().name("Denver Nuggets").url("dn_url_id").build();
		Team t2 = Team.builder().name("Golden State Warriors").url("gsw_url_id").build();
		teamMapper.insert(t1);
		teamMapper.insert(t2);

		assertEquals(2, teamMapper.getAll().size());
		assertEquals("Denver Nuggets", teamMapper.getAll().get(0).getName());
		assertEquals("Golden State Warriors", teamMapper.getAll().get(1).getName());

	}
	
}