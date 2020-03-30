package com.mozzartbet.gameservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

import com.mozzartbet.gameservice.dao.PlayerDao;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.PlayerException;
import com.mozzartbet.gameservice.setup.PlayerSetup;

public class PlayerServiceMockTest extends BaseServiceTest {

	@Autowired
	private PlayerSetup playerSetup;

	@Autowired
	private PlayerService playerService;
	
	@MockBean
	private PlayerDao playerDao;
	
	@Test
	public void testDuplicateException_Mock() {
		
		when(playerDao.insert(Mockito.any())).thenThrow(new DuplicateKeyException("Duplicated in test"));
		
		assertThrows(PlayerException.class, () -> {
			playerService.save(Player.builder().team(Team.builder().id(1L).build()).name("Nikola Jokic").number("4").position("PG").build());
		});
	}
	
	@Test
	public void testGetById_CaffeineCached_Mock() {
		
		List<Player> ps = playerSetup.getSetup();
		
		Long id1 = ps.get(0).getId();
		when(playerDao.getById(id1)).thenReturn(ps.get(0));
		Long id2 = ps.get(1).getId();
		when(playerDao.getById(id2)).thenReturn(ps.get(1));

		for (int i = 0; i < 3; i++) {
			playerService.getById(id1);
			playerService.getById(id2);
			
		}

		verify(playerDao, times(2)).getById(Mockito.anyLong());
		verify(playerDao, times(1)).getById(id1);
		verify(playerDao, times(1)).getById(id2);
	}
}
