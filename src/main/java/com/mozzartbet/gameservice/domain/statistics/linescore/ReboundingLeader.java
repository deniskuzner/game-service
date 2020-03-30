package com.mozzartbet.gameservice.domain.statistics.linescore;

import com.mozzartbet.gameservice.domain.Player;

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
public class ReboundingLeader extends LineScoreLeader {
	
	private long matchId;
	private String quarter;
	private Player player;
	private int rebounds;

}
