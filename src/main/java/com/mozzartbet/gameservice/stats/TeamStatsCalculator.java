package com.mozzartbet.gameservice.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;

public class TeamStatsCalculator {

	private Match match;
	private List<PlayerStats> playersStats;

	public TeamStatsCalculator(Match match, List<PlayerStats> playersStats) {
		this.match = match;
		this.playersStats = playersStats;
	}

	public List<TeamStats> getTeamStats() {

		// Odvajanje svih igraca na 2 tima
		List<PlayerStats> hostPlayersStats = playersStats.stream().filter(playerStats -> playerStats.getPlayer().getTeam().getUrl().equals(match.getHost().getUrl())).collect(Collectors.toList());
		List<PlayerStats> guestPlayersStats = playersStats.stream().filter(playerStats -> playerStats.getPlayer().getTeam().getUrl().equals(match.getGuest().getUrl())).collect(Collectors.toList());

		// Racunanje statistika za oba tima
		TeamStats hostTeamStats = calculateTeamStats(hostPlayersStats);
		TeamStats guestTeamStats = calculateTeamStats(guestPlayersStats);

		System.out.println(hostTeamStats);
		System.out.println(guestTeamStats);

		return Collections.unmodifiableList(new ArrayList<>(Arrays.asList(hostTeamStats, guestTeamStats)));
	}

	public TeamStats calculateTeamStats(List<PlayerStats> playersStats) {
		
		  Team team = playersStats.get(0).getPlayer().getTeam(); 
		  //String minutesPlayed; 
		  int fieldGoals = playersStats.stream().mapToInt(x -> x.getFieldGoals()).sum(); 
		  int fieldGoalAttempts = playersStats.stream().mapToInt(x -> x.getFieldGoalAttempts()).sum(); 
		  double fieldGoalPercentage = fieldGoalAttempts == 0 ? 0 : Math.round((double) fieldGoals/fieldGoalAttempts*1000.0)/1000.0; 
		  int threePointFieldGoals = playersStats.stream().mapToInt(x -> x.getThreePointFieldGoals()).sum(); 
		  int threePointFieldGoalAttempts = playersStats.stream().mapToInt(x -> x.getThreePointFieldGoalAttempts()).sum(); 
		  double threePointFieldGoalPercentage = threePointFieldGoalAttempts == 0 ? 0 : Math.round((double) threePointFieldGoals/threePointFieldGoalAttempts*1000.0)/1000.0; 
		  int freeThrows = playersStats.stream().mapToInt(x -> x.getFreeThrows()).sum();
		  int freeThrowAttempts = playersStats.stream().mapToInt(x -> x.getFreeThrowAttempts()).sum(); 
		  double freeThrowPercentage = freeThrowAttempts == 0 ? 0 : Math.round((double) freeThrows/freeThrowAttempts*1000.0)/1000.0; 
		  int offensiveRebounds = playersStats.stream().mapToInt(x -> x.getOffensiveRebounds()).sum(); 
		  int defensiveRebounds = playersStats.stream().mapToInt(x -> x.getDefensiveRebounds()).sum(); int totalRebounds = offensiveRebounds + defensiveRebounds; 
		  int assists = playersStats.stream().mapToInt(x -> x.getAssists()).sum(); 
		  int steals = playersStats.stream().mapToInt(x -> x.getSteals()).sum(); 
		  int blocks = playersStats.stream().mapToInt(x -> x.getBlocks()).sum(); 
		  int turnovers = playersStats.stream().mapToInt(x -> x.getTurnovers()).sum(); 
		  int personalFouls = playersStats.stream().mapToInt(x -> x.getPersonalFouls()).sum(); 
		  int points = playersStats.stream().mapToInt(x -> x.getPoints()).sum();
		 
		
		return TeamStats.builder()
				.team(team).minutesPlayed(null).fieldGoals(fieldGoals).fieldGoalAttempts(fieldGoalAttempts).fieldGoalPercentage(fieldGoalPercentage)
				.threePointFieldGoals(threePointFieldGoals).threePointFieldGoalAttempts(threePointFieldGoalAttempts).threePointFieldGoalPercentage(threePointFieldGoalPercentage)
				.freeThrows(freeThrows).freeThrowAttempts(freeThrowAttempts).freeThrowPercentage(freeThrowPercentage)
				.offensiveRebounds(offensiveRebounds).defensiveRebounds(defensiveRebounds).totalRebounds(totalRebounds)
				.assists(assists).steals(steals).blocks(blocks).turnovers(turnovers).personalFouls(personalFouls).points(points).build();
	}

}
