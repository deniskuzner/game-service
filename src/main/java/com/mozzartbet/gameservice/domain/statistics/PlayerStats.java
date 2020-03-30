package com.mozzartbet.gameservice.domain.statistics;

import java.time.LocalDateTime;

import com.mozzartbet.gameservice.domain.BaseEntity;
import com.mozzartbet.gameservice.domain.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class PlayerStats implements BaseEntity{
	
	private Long id;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	private Long matchId;
	private Player player;
	private String minutesPlayed;
	private int fieldGoals;
	private int fieldGoalAttempts;
	private double fieldGoalPercentage;
	private int threePointFieldGoals;
	private int threePointFieldGoalAttempts;
	private double threePointFieldGoalPercentage;
	private int freeThrows;
	private int freeThrowAttempts;
	private double freeThrowPercentage;
	private int offensiveRebounds;
	private int defensiveRebounds;
	private int totalRebounds;
	private int assists;
	private int steals;
	private int blocks;
	private int turnovers;
	private int personalFouls;
	private int points;
	private String plusMinus;

}