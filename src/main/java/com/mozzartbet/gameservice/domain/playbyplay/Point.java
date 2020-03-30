package com.mozzartbet.gameservice.domain.playbyplay;

import com.mozzartbet.gameservice.domain.Player;

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
public class Point extends PlayByPlayAction {

	private int points;
	private Player pointPlayer;
	private Player assistPlayer;

}
