package com.mozzartbet.gameservice.domain.playbyplay;

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
public class QuarterInfo extends PlayByPlayAction {

	private String jumper1Id;
	private String jumer2Id;
	private String possessionPlayerId;

}
