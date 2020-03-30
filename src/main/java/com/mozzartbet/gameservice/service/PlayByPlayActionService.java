package com.mozzartbet.gameservice.service;

import java.util.List;
import java.util.concurrent.Future;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.mozzartbet.gameservice.message.PlayByPlayActionMessage;
import com.mozzartbet.gameservice.service.dto.SendPbpActionsRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface PlayByPlayActionService {

	@RequiredArgsConstructor
	@Getter
	public enum PbpActionDestination {
		STATS_PLAY_BY_PLAY("game-service.stats-play-by-play");
		
		private final String destinationName;
	}
	
	public @NotNull Future<List<PlayByPlayActionMessage>> sendPpbActions(@NotNull @Valid SendPbpActionsRequest request);
	
}
