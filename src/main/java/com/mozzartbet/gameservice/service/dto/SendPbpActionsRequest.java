package com.mozzartbet.gameservice.service.dto;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import com.mozzartbet.gameservice.service.PlayByPlayActionService.PbpActionDestination;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SendPbpActionsRequest {

	@NotNull
	final PbpActionDestination destination;
	@NotNull
	final LocalTime relativeTo;
	@NotNull
	FindPbpActionsRequest findPbpActionsRequest;

	Double speedFactor;

	public String getDestinationName() {
		return destination.getDestinationName();
	}

	public long calculateTimeToSleep(long timeToSleep) {
		return (speedFactor == null) ? timeToSleep : Math.round(timeToSleep * speedFactor);
	}

}
