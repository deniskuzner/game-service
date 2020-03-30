package com.mozzartbet.gameservice.domain.statistics.linescore.dto;

import com.mozzartbet.gameservice.domain.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@SuperBuilder
public class TeamQuarterPoints {

	private String quarter;
	private Team team;
	private int points;
	private Long matchId;
	
}
