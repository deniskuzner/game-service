package com.mozzartbet.gameservice.service.dto;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FindPbpActionsRequest {

	final LocalTime fromDate;
	
	final LocalTime toDate;

	final String quarter;
	@NotNull
	final long matchId;
}
