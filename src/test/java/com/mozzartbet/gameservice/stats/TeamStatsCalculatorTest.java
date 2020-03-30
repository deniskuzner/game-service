package com.mozzartbet.gameservice.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.TeamStats;
import com.mozzartbet.gameservice.parser.MatchPageHtmlParser;

class TeamStatsCalculatorTest {

	private Match match = new MatchPageHtmlParser("201905190TOR").parse();
	private PlayerStatsCalculator playerStatsCalculator = new PlayerStatsCalculator(match);
	private TeamStatsCalculator calculator = new TeamStatsCalculator(match, playerStatsCalculator.getPlayerStats());
	private List<PlayerStats> hostPlayersStats = playerStatsCalculator.getPlayerStats().stream()
			.filter(playerStats -> playerStats.getPlayer().getTeam().getUrl().equals(match.getHost().getUrl()))
			.collect(Collectors.toList());
	private List<PlayerStats> guestPlayersStats = playerStatsCalculator.getPlayerStats().stream()
			.filter(playerStats -> playerStats.getPlayer().getTeam().getUrl().equals(match.getGuest().getUrl()))
			.collect(Collectors.toList());

	@Test
	public void testGetTeamStats() {
		assertEquals(2, calculator.getTeamStats().size());
	}

	@Test
	public void testHostPropertyValues() {
		TeamStats teamStats = calculator.calculateTeamStats(hostPlayersStats);
		assertEquals(teamStats.getFieldGoals(), 38);
		assertEquals(teamStats.getFieldGoalAttempts(), 102);
		assertEquals(teamStats.getFieldGoalPercentage(), 0.373);
		assertEquals(teamStats.getThreePointFieldGoals(), 14);
		assertEquals(teamStats.getThreePointFieldGoalAttempts(), 44);
		assertEquals(teamStats.getThreePointFieldGoalPercentage(), 0.318);
		assertEquals(teamStats.getFreeThrows(), 22);
		assertEquals(teamStats.getFreeThrowAttempts(), 33);
		assertEquals(teamStats.getFreeThrowPercentage(), 0.667);
		assertEquals(teamStats.getOffensiveRebounds(), 13);
		assertEquals(teamStats.getDefensiveRebounds(), 50);
		assertEquals(teamStats.getTotalRebounds(), 63);
		assertEquals(teamStats.getAssists(), 22);
		assertEquals(teamStats.getSteals(), 14);
		assertEquals(teamStats.getBlocks(), 5);
		assertEquals(teamStats.getTurnovers(), 20);
		assertEquals(teamStats.getPersonalFouls(), 30);
		assertEquals(teamStats.getPoints(), 112);
	}

	@Test
	public void testGeustPropertyValues() {
		TeamStats teamStats = calculator.calculateTeamStats(guestPlayersStats);
		assertEquals(teamStats.getFieldGoals(), 40);
		assertEquals(teamStats.getFieldGoalAttempts(), 102);
		assertEquals(teamStats.getFieldGoalPercentage(), 0.392);
		assertEquals(teamStats.getThreePointFieldGoals(), 17);
		assertEquals(teamStats.getThreePointFieldGoalAttempts(), 45);
		assertEquals(teamStats.getThreePointFieldGoalPercentage(), 0.378);
		assertEquals(teamStats.getFreeThrows(), 21);
		assertEquals(teamStats.getFreeThrowAttempts(), 26);
		assertEquals(teamStats.getFreeThrowPercentage(), 0.808);
		assertEquals(teamStats.getOffensiveRebounds(), 8);
		assertEquals(teamStats.getDefensiveRebounds(), 47);
		assertEquals(teamStats.getTotalRebounds(), 55);
		assertEquals(teamStats.getAssists(), 28);
		assertEquals(teamStats.getSteals(), 11);
		assertEquals(teamStats.getBlocks(), 10);
		assertEquals(teamStats.getTurnovers(), 17);
		assertEquals(teamStats.getPersonalFouls(), 30);
		assertEquals(teamStats.getPoints(), 118);
	}

	/*
	 * @Test
	 * 
	 * @DisplayName("Should have the correct property values") void
	 * shouldHaveCorrectPropertyValues() {
	 * assertThat(calculator.calculateTeamStats(hostPlayersStats),
	 * allOf(hasProperty("team", is(match.getHost())), hasProperty("fieldGoals",
	 * is(38)), hasProperty("fieldGoalAttempts", is(102)),
	 * hasProperty("fieldGoalPercentage", is(0.373)),
	 * hasProperty("threePointFieldGoals", is(14)),
	 * hasProperty("threePointFieldGoalAttempts", is(44)),
	 * hasProperty("threePointFieldGoalPercentage", is(0.318)),
	 * hasProperty("freeThrows", is(22)), hasProperty("freeThrowAttempts", is(33)),
	 * hasProperty("freeThrowPercentage", is(0.667)),
	 * hasProperty("offensiveRebounds", is(13)), hasProperty("defensiveRebounds",
	 * is(50)), hasProperty("totalRebounds", is(63)), hasProperty("assists",
	 * is(22)), hasProperty("steals", is(14)), hasProperty("blocks", is(5)),
	 * hasProperty("turnovers", is(20)), hasProperty("personalFouls", is(30)),
	 * hasProperty("points", is(112)))); }
	 */

}
