package com.mozzartbet.gameservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.setup.PlayerSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class PlayerServiceLatchLockTest{

	@Autowired
	PlayerSetup playerSetup;
	
	@Autowired
	PlayerService playerService;
	
	@Test
	@Commit
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = {
			"delete from players",
			"delete from teams"
	})
	public void testTwoThreadsSave() throws InterruptedException {
		
		playerSetup.getSetup();
		
		ExecutorService pool = Executors.newFixedThreadPool(2);
		PlayerThread1 pt1 = new PlayerThread1();
		PlayerThread2 pt2 = new PlayerThread2();
		pool.invokeAll(Arrays.asList(pt1, pt2));
		
		assertEquals(pt1.p.getName(), "Davis Bertans 1");
		assertEquals(pt2.caught.getClass(), OptimisticLockingFailureException.class);
		
		Player p = playerService.getById(pt1.p.getId());
		assertEquals(p.getName(), "Davis Bertans 1");
		
	}
	
	final CountDownLatch countDownLatch = new CountDownLatch(1);
	
	class PlayerThread1 implements Callable<Player> {
		
		Player p;

		@Override
		public Player call() throws Exception {
			p = playerService.getAll().get(0);
			p.setName(p.getName() + " 1");
			
			p = playerService.save(p);
			countDownLatch.countDown();
			
			return p;
		}
	}

	class PlayerThread2 implements Callable<Player> {
		
		Player p;
		Exception caught;

		@Override
		public Player call() throws Exception {
			p = playerService.getAll().get(0);
			p.setName(p.getName() + " 2");
			
			try {
				countDownLatch.await();
				p = playerService.save(p);
			} catch (OptimisticLockingFailureException e) {
				caught = e;
			}
			
			return p;
		}
	}


}
