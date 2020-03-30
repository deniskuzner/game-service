package com.mozzartbet.gameservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

import com.mozzartbet.gameservice.dao.TeamDao;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.TeamException;

class TeamServiceMockTest extends BaseServiceTest {

	@Autowired
	TeamService teamService;
	
	@MockBean
	TeamDao teamDao;

	//Mocking
	@Test
	public void testDuplicateException_WithMock() {
		
		when(teamDao.save(Mockito.any())).thenThrow(new DuplicateKeyException("Duplicated in test"));

		assertThrows(TeamException.class, () -> {
			teamService.save(Team.builder().id(1l).name("name").url("url").build());
		});
	}
}
