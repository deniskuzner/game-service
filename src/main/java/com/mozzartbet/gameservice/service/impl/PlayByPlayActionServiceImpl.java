package com.mozzartbet.gameservice.service.impl;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mozzartbet.gameservice.dao.PlayByPlayActionDao;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.message.PlayByPlayActionMessage;
import com.mozzartbet.gameservice.message.PlayByPlayActionMessageConverter;
import com.mozzartbet.gameservice.service.PlayByPlayActionService;
import com.mozzartbet.gameservice.service.dto.SendPbpActionsRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class PlayByPlayActionServiceImpl implements PlayByPlayActionService {

	@Autowired
	PlayByPlayActionDao playByPlayActionDao;

	@Autowired
	JmsTemplate jmsTempalte;
	
	@Autowired
	PlayByPlayActionMessageConverter pbpActionMessageConverter;

	private void sleepMillis(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	@Transactional
	@Async
	public Future<List<PlayByPlayActionMessage>> sendPpbActions(SendPbpActionsRequest request) {
		List<PlayByPlayAction> pbpActions = playByPlayActionDao.findPbpActions(request.getFindPbpActionsRequest(), 0);
		
		List<PlayByPlayActionMessage> result = new ArrayList<>();

		LocalTime prev = request.getRelativeTo();

		for (PlayByPlayAction pbpAction : pbpActions) {
			//long sleepTime = ChronoUnit.MILLIS.between(pbpAction.getTimestamp(), prev);
			long sleepTime = getTimeDifference(prev, pbpAction);
			sleepMillis(request.calculateTimeToSleep(sleepTime));
			
			if(playByPlayActionDao.updateSent(pbpAction.getId()) == 1) {
				PlayByPlayActionMessage message = pbpActionMessageConverter.convert(pbpAction);
				result.add(message);
				jmsTempalte.convertAndSend(request.getDestinationName(), message);
			}
			prev = pbpAction.getTimestamp();
		}
		
		return AsyncResult.forValue(result);
	}
	
	
	//uraditi timeDifference
	private long getTimeDifference(LocalTime prev, PlayByPlayAction action) {
		long timeDifference = ChronoUnit.MILLIS.between(action.getTimestamp(), prev);
		if(action.getTimestamp().isAfter(prev) && action.getQuarter().contains("OT")) {
			timeDifference = ChronoUnit.MILLIS.between(LocalTime.parse("00:00:00.0"), prev) + ChronoUnit.MILLIS.between(action.getTimestamp(), LocalTime.parse("00:12:00.0"));
		}
		if(action.getTimestamp().isAfter(prev) && !action.getQuarter().contains("OT")) {
			timeDifference = ChronoUnit.MILLIS.between(LocalTime.parse("00:00:00.0"), prev) + ChronoUnit.MILLIS.between(action.getTimestamp(), LocalTime.parse("00:5:00.0"));
		}
		return timeDifference;
		
	}
}
