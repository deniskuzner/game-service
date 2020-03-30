package com.mozzartbet.gameservice.service;

import static com.mozzartbet.gameservice.service.PlayByPlayActionService.PbpActionDestination.STATS_PLAY_BY_PLAY;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.message.PlayByPlayActionMessage;
import com.mozzartbet.gameservice.service.PlayByPlayActionService.PbpActionDestination;
import com.mozzartbet.gameservice.service.dto.FindPbpActionsRequest;
import com.mozzartbet.gameservice.service.dto.SendPbpActionsRequest;
import com.mozzartbet.gameservice.setup.PlayByPlayActionSetup;
import com.mozzartbet.gameservice.setup.PlayerSetup;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class PlayByPlayActionServiceTest {

	@Autowired
	PlayByPlayActionService pbpActionService;

	@Autowired
	PlayerSetup playerSetup;

	@Autowired
	PlayByPlayActionSetup pbpActionSetup;

	@Autowired
	JmsTemplate jmsTemplate;

	@Test
	@Sql(executionPhase = AFTER_TEST_METHOD, statements = { "delete from play_by_play_actions", "delete from players",
			"delete from teams" })
	public void sendEventsAndManuallyRecieve() throws JMSException, JSONException {
		List<Player> ps = playerSetup.getSetup();
		List<PlayByPlayAction> as = pbpActionSetup.getSetup(ps);

		long action1Id = as.get(0).getId();
		long action2Id = as.get(1).getId();
		long action3Id = as.get(2).getId();
		long teamId = as.get(0).getTeam().getId();
		long player1Id = ps.get(0).getId();
		long player2Id = ps.get(1).getId();
		
		FindPbpActionsRequest findRequest = FindPbpActionsRequest.builder().fromDate(LocalTime.parse("00:10:11.0"))
				.toDate(LocalTime.parse("00:12:00.0")).quarter("1st Q").matchId(1l).build();

		pbpActionService.sendPpbActions(SendPbpActionsRequest.builder().destination(STATS_PLAY_BY_PLAY)
				.relativeTo(LocalTime.parse("00:12:00.0")).speedFactor(0.1).findPbpActionsRequest(findRequest).build());

		Message m;

		m = jmsTemplate.receive(PbpActionDestination.STATS_PLAY_BY_PLAY.getDestinationName());

		log.info("poruka:{}", ((ActiveMQTextMessage) m).getText());

		assertEquals(String.format(
				"{'id':%s,'matchId':1,'teamId':%s,'timestamp':'00:12:00','sumScore':'2:0','points':2,'pointPlayerId':%s,'assistPlayerId':%s}", action1Id, teamId, player1Id, player2Id),
				((ActiveMQTextMessage) m).getText(), false);

		m = jmsTemplate.receive(PbpActionDestination.STATS_PLAY_BY_PLAY.getDestinationName());

		log.info("poruka:{}", ((ActiveMQTextMessage) m).getText());

		assertEquals(String.format(
				"{'id':%s,'matchId':1,'teamId':%s,'timestamp':'00:11:55','sumScore':'2:3','playerId':%s}", action2Id, teamId, player2Id),
				((ActiveMQTextMessage) m).getText(), false);

		m = jmsTemplate.receive(PbpActionDestination.STATS_PLAY_BY_PLAY.getDestinationName());

		log.info("poruka:{}", ((ActiveMQTextMessage) m).getText());

		assertEquals(String.format(
				"{'id':%s,'matchId':1,'teamId':%s,'timestamp':'00:11:50','sumScore':'5:3','playerId':%s}", action3Id, teamId, player1Id),
				((ActiveMQTextMessage) m).getText(), false);

	}

	@Test
	@Sql(executionPhase = AFTER_TEST_METHOD, statements = { "delete from play_by_play_actions", "delete from players",
			"delete from teams" })
	public void sendEventsAndManuallyRecieveAndConvert()
			throws JMSException, JSONException, InterruptedException, ExecutionException {
		List<Player> ps = playerSetup.getSetup();
		pbpActionSetup.getSetup(ps);

		assertEquals(pbpActionSetup.countSent(0), 6l);
		
		FindPbpActionsRequest findRequest = FindPbpActionsRequest.builder().fromDate(LocalTime.parse("00:10:11.0"))
				.toDate(LocalTime.parse("00:12:00.0")).quarter("1st Q").matchId(1l).build();

		Future<List<PlayByPlayActionMessage>> actions = pbpActionService.sendPpbActions(SendPbpActionsRequest.builder().destination(STATS_PLAY_BY_PLAY)
				.relativeTo(LocalTime.parse("00:12:00.0")).speedFactor(0.1).findPbpActionsRequest(findRequest).build());

		List<PlayByPlayActionMessage> res = new ArrayList<>();
		res.add((PlayByPlayActionMessage) jmsTemplate.receiveAndConvert(STATS_PLAY_BY_PLAY.getDestinationName()));
		res.add((PlayByPlayActionMessage) jmsTemplate.receiveAndConvert(STATS_PLAY_BY_PLAY.getDestinationName()));
		res.add((PlayByPlayActionMessage) jmsTemplate.receiveAndConvert(STATS_PLAY_BY_PLAY.getDestinationName()));

		assertEquals(actions.get().get(0).getTimestamp(), res.get(0).getTimestamp());
		assertEquals(actions.get().get(1).getTimestamp(), res.get(1).getTimestamp());
		assertEquals(actions.get().get(2).getTimestamp(), res.get(2).getTimestamp());

		assertEquals(pbpActionSetup.countSent(1), 3l);

	}

}
