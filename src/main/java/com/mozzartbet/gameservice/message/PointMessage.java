package com.mozzartbet.gameservice.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PointMessage extends PlayByPlayActionMessage {

	int points;
	long pointPlayerId;
	long assistPlayerId;

}
