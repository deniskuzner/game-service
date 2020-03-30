package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;
import java.util.List;

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
public class Season implements BaseEntity {

	private Long id;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	
	private int year;
	private String league;
	private List<Match> matches;
	private List<Player> players;
	
}
