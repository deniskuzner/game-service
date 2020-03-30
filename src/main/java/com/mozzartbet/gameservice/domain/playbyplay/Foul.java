package com.mozzartbet.gameservice.domain.playbyplay;

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
public class Foul extends PlayByPlayAction {

	private Player foulByPlayer;

}
