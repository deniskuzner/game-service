package com.mozzartbet.gameservice.message;

import java.time.LocalTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class PlayByPlayActionMessage {
	
	private long id;
	
	@EqualsAndHashCode.Include
	private long matchId;
	@EqualsAndHashCode.Include
	private long teamId;
	@EqualsAndHashCode.Include
	private LocalTime timestamp;
	@EqualsAndHashCode.Include
	private String sumScore;
	@EqualsAndHashCode.Include
	private String quarter;
	
}
