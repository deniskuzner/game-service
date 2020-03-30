package com.mozzartbet.gameservice.domain.statistics;

import java.util.List;

import com.mozzartbet.gameservice.domain.Match;

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
public class MatchStats {
	
	private Match match;
	private List<PlayerStats> playerStats;
	private List<TeamStats> teamStats;
	private LineScore lineScore;

}
