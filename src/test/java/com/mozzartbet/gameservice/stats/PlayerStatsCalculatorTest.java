package com.mozzartbet.gameservice.stats;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mozzartbet.gameservice.parser.MatchPageHtmlParser;

class PlayerStatsCalculatorTest {

	private MatchPageHtmlParser parser = new MatchPageHtmlParser("201905190TOR");
	private PlayerStatsCalculator calculator = new PlayerStatsCalculator(parser.parse());


	@Test
	@DisplayName("Number of player stats should be 26")
	public void testGetPlayerStats() {
		assertEquals(26, calculator.getPlayerStats().size());
	}
	
	@Test
	@DisplayName("Should have correct field goals")
	public void gettingtFieldGoals() {
		assertEquals(calculator.getPlayerStats().get(0).getFieldGoals(), 5); 
	}
	
	@Test
	@DisplayName("Should have correct field goal attempts")
	public void gettingFieldGoalAttempts() {
		assertEquals(calculator.getPlayerStats().get(0).getFieldGoalAttempts(), 16); 
	}
	
	@Test
	@DisplayName("Should have correct field goal percentage")
	public void gettingFieldGoalPercentage() {
		assertEquals(calculator.getPlayerStats().get(0).getFieldGoalPercentage(), 0.313); 
	}
	
	@Test
	@DisplayName("Should have correct three point field goals")
	public void gettingThreePointFieldGoals() {
		assertEquals(calculator.getPlayerStats().get(0).getThreePointFieldGoals(), 0); 
	}
	
	@Test
	@DisplayName("Should have correct three point field goal attempts")
	public void gettingThreePointFieldGoalAttempts() {
		assertEquals(calculator.getPlayerStats().get(0).getThreePointFieldGoalAttempts(), 3); 
	}
	
	@Test
	@DisplayName("Should have correct three point field goal percentage")
	public void gettingThreePointFieldGoalPercentage() {
		assertEquals(calculator.getPlayerStats().get(0).getThreePointFieldGoalPercentage(), 0.000); 
	}
	
	@Test
	@DisplayName("Should have correct free throws")
	public void gettingFreeThrows() {
		assertEquals(calculator.getPlayerStats().get(0).getFreeThrows(), 2); 
	}
	
	@Test
	@DisplayName("Should have correct free throw attempts")
	public void gettingFreeThrowAttempts() {
		assertEquals(calculator.getPlayerStats().get(0).getFreeThrowAttempts(), 7); 
	}
	
	@Test
	@DisplayName("Should have correct free throw percentage")
	public void gettingFreeThrowPercentage() {
		assertEquals(calculator.getPlayerStats().get(0).getFreeThrowPercentage(), 0.286); 
	}
	
	@Test
	@DisplayName("Should have correct offensive rebounds")
	public void gettingOffensiveRebounds() {
		assertEquals(calculator.getPlayerStats().get(0).getOffensiveRebounds(), 3); 
	}
	
	@Test
	@DisplayName("Should have correct defensive rebounds")
	public void gettingDefensiveRebounds() {
		assertEquals(calculator.getPlayerStats().get(0).getDefensiveRebounds(), 20); 
	}
	
	@Test
	@DisplayName("Should have correct total rebounds")
	public void gettingTotalRebounds() {
		assertEquals(calculator.getPlayerStats().get(0).getTotalRebounds(), 23); 
	}
	
	@Test
	@DisplayName("Should have correct assists")
	public void gettingAssists() {
		assertEquals(calculator.getPlayerStats().get(0).getAssists(), 7); 
	}
	
	@Test
	@DisplayName("Should have correct steals")
	public void gettingSteals() {
		assertEquals(calculator.getPlayerStats().get(0).getSteals(), 1); 
	}
	
	@Test
	@DisplayName("Should have correct blocks")
	public void gettingBlocks() {
		assertEquals(calculator.getPlayerStats().get(0).getBlocks(), 4); 
	}
	
	@Test
	@DisplayName("Should have correct turnovers")
	public void gettingTurnovers() {
		assertEquals(calculator.getPlayerStats().get(0).getTurnovers(), 8); 
	}
	
	@Test
	@DisplayName("Should have correct personal fouls")
	public void gettingPersonalFouls() {
		assertEquals(calculator.getPlayerStats().get(0).getPersonalFouls(), 6); 
	}
	
	@Test
	@DisplayName("Should have correct points")
	public void gettingPoints() {
		assertEquals(calculator.getPlayerStats().get(0).getPoints(), 12); 
	}
	
	@Test
    @DisplayName("Should have the correct property values")
    void shouldHaveCorrectPropertyValues() {
        assertThat(calculator.getPlayerStats().get(0), allOf(
                hasProperty("fieldGoals", is(5)),
                hasProperty("fieldGoalAttempts", is(16)),
                hasProperty("fieldGoalPercentage", is(0.313)),
                hasProperty("threePointFieldGoals", is(0)),
                hasProperty("threePointFieldGoalAttempts", is(3)),
                hasProperty("threePointFieldGoalPercentage", is(0.000)),
                hasProperty("freeThrows", is(2)),
                hasProperty("freeThrowAttempts", is(7)),
                hasProperty("freeThrowPercentage", is(0.286)),
                hasProperty("offensiveRebounds", is(3)),
                hasProperty("defensiveRebounds", is(20)),
                hasProperty("totalRebounds", is(23)),
                hasProperty("assists", is(7)),
                hasProperty("steals", is(1)),
                hasProperty("blocks", is(4)),
                hasProperty("turnovers", is(8)),
                hasProperty("personalFouls", is(6)),
                hasProperty("points", is(12))
        ));
    }

}
