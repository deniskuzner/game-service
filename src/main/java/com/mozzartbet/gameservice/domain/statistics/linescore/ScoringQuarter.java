package com.mozzartbet.gameservice.domain.statistics.linescore;

import java.time.LocalDateTime;

import com.mozzartbet.gameservice.domain.BaseEntity;
import com.mozzartbet.gameservice.domain.Match;

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
public class ScoringQuarter implements BaseEntity {

	private Long id;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	private Match match;
	private String quarter;
	private int hostPoints;
	private int guestPoints;

}
