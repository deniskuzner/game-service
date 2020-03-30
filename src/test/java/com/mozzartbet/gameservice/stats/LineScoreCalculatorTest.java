package com.mozzartbet.gameservice.stats;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.AssistLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ReboundingLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;
import com.mozzartbet.gameservice.parser.MatchPageHtmlParser;

class LineScoreCalculatorTest {

	private Match match = new MatchPageHtmlParser(new File("src/test/resources/com/mozzartbet/gameservice/parser/"
			+ "Milwaukee Bucks at Toronto Raptors Play-By-Play, May 19, 2019 _ Basketball-Reference.com.html")).parse();
	private List<PlayerStats> playersStats = new PlayerStatsCalculator(match).getPlayerStats();
	private LineScoreCalculator calculator = new LineScoreCalculator(match, playersStats);
	
	@Test
	void testGetQuarters() {
		assertEquals(7, calculator.getLineScore().getScoringQuarters().size());
	}
	
	@Test
	void testGetQuarterPoints() {
		List<ScoringQuarter> quarters = calculator.getLineScore().getScoringQuarters();
		assertEquals(21, quarters.get(0).getHostPoints());
		assertEquals(30, quarters.get(1).getHostPoints());
		assertEquals(24, quarters.get(2).getHostPoints());
		assertEquals(21, quarters.get(3).getHostPoints());
		assertEquals(7, quarters.get(4).getHostPoints());
		assertEquals(9, quarters.get(5).getHostPoints());
		assertEquals(112, quarters.get(6).getHostPoints());
		
		assertEquals(30, quarters.get(0).getGuestPoints());
		assertEquals(28, quarters.get(1).getGuestPoints());
		assertEquals(19, quarters.get(2).getGuestPoints());
		assertEquals(19, quarters.get(3).getGuestPoints());
		assertEquals(7, quarters.get(4).getGuestPoints());
		assertEquals(15, quarters.get(5).getGuestPoints());
		assertEquals(118, quarters.get(6).getGuestPoints());
	}
	
	@Test
	void testGetScoringLeaders() {
		List<ScoringLeader> scoringLeaders = calculator.getLineScore().getScoringLeaders();
		assertEquals(7, scoringLeaders.size());
		
		assertEquals(scoringLeaders.get(0).getQuarter(), "1st Q");
		assertEquals(scoringLeaders.get(0).getPlayer().getUrl(), "p/powelno01");
		assertEquals(scoringLeaders.get(0).getPoints(), 10);
		
		assertEquals(scoringLeaders.get(1).getQuarter(), "2nd Q");
		assertEquals(scoringLeaders.get(1).getPlayer().getUrl(), "l/leonaka01");
		assertEquals(scoringLeaders.get(1).getPoints(), 11);
		
		assertEquals(scoringLeaders.get(2).getQuarter(), "3rd Q");
		assertEquals(scoringLeaders.get(2).getPlayer().getUrl(), "s/siakapa01");
		assertEquals(scoringLeaders.get(2).getPoints(), 8);
		
		assertEquals(scoringLeaders.get(3).getQuarter(), "4th Q");
		assertEquals(scoringLeaders.get(3).getPlayer().getUrl(), "l/leonaka01");
		assertEquals(scoringLeaders.get(3).getPoints(), 9);
		
		assertEquals(scoringLeaders.get(4).getQuarter(), "1st OT");
		assertEquals(scoringLeaders.get(4).getPlayer().getUrl(), "h/hillge01");
		assertEquals(scoringLeaders.get(4).getPoints(), 5);
		
		assertEquals(scoringLeaders.get(5).getQuarter(), "2nd OT");
		assertEquals(scoringLeaders.get(5).getPlayer().getUrl(), "l/leonaka01");
		assertEquals(scoringLeaders.get(5).getPoints(), 8);
		
		assertEquals(scoringLeaders.get(6).getQuarter(), "Tot");
		assertEquals(scoringLeaders.get(6).getPlayer().getUrl(), "l/leonaka01");
		assertEquals(scoringLeaders.get(6).getPoints(), 36);
	}
	
	@Test
	void testGetScoringLeadersHamcrest() {
		List<ScoringLeader> scoringLeaders = calculator.getLineScore().getScoringLeaders();
		assertThat(scoringLeaders.get(0), allOf(
                hasProperty("quarter", is("1st Q")),
                hasProperty("playerUrl", is("p/powelno01")),
                hasProperty("points", is(10))
        ));
		assertThat(scoringLeaders.get(1), allOf(
                hasProperty("quarter", is("2st Q")),
                hasProperty("playerUrl", is("l/leonaka01")),
                hasProperty("points", is(11))
        ));
		assertThat(scoringLeaders.get(2), allOf(
                hasProperty("quarter", is("3st Q")),
                hasProperty("playerUrl", is("s/siakapa01")),
                hasProperty("points", is(8))
        ));
		assertThat(scoringLeaders.get(3), allOf(
                hasProperty("quarter", is("4st Q")),
                hasProperty("playerUrl", is("l/leonaka01")),
                hasProperty("points", is(9))
        ));
		assertThat(scoringLeaders.get(4), allOf(
                hasProperty("quarter", is("1st OT")),
                hasProperty("playerUrl", is("h/hillge01")),
                hasProperty("points", is(5))
        ));
		assertThat(scoringLeaders.get(5), allOf(
                hasProperty("quarter", is("2nd OT")),
                hasProperty("playerUrl", is("l/leonaka01")),
                hasProperty("points", is(8))
        ));
		assertThat(scoringLeaders.get(6), allOf(
                hasProperty("quarter", is("Tot")),
                hasProperty("playerUrl", is("l/leonaka01")),
                hasProperty("points", is(36))
        ));
	}
	
	@Test
	void testGetReboundingLeaders() {
		List<ReboundingLeader> reboundingLeaders = calculator.getLineScore().getReboundingLeaders();
		
		assertEquals(7, reboundingLeaders.size());
		
		assertEquals(reboundingLeaders.get(0).getQuarter(), "1st Q");
		assertEquals(reboundingLeaders.get(0).getPlayer().getUrl(), "s/siakapa01");
		assertEquals(reboundingLeaders.get(0).getRebounds(), 5);
		
		assertEquals(reboundingLeaders.get(1).getQuarter(), "2nd Q");
		assertEquals(reboundingLeaders.get(1).getPlayer().getUrl(), "2 tied");
		assertEquals(reboundingLeaders.get(1).getRebounds(), 3);
		
		assertEquals(reboundingLeaders.get(2).getQuarter(), "3rd Q");
		assertEquals(reboundingLeaders.get(2).getPlayer().getUrl(), "a/antetgi01");
		assertEquals(reboundingLeaders.get(2).getRebounds(), 7);
		
		assertEquals(reboundingLeaders.get(3).getQuarter(), "4th Q");
		assertEquals(reboundingLeaders.get(3).getPlayer().getUrl(), "a/antetgi01");
		assertEquals(reboundingLeaders.get(3).getRebounds(), 6);
		
		assertEquals(reboundingLeaders.get(4).getQuarter(), "1st OT");
		assertEquals(reboundingLeaders.get(4).getPlayer().getUrl(), "a/antetgi01");
		assertEquals(reboundingLeaders.get(4).getRebounds(), 3);
		
		assertEquals(reboundingLeaders.get(5).getQuarter(), "2nd OT");
		assertEquals(reboundingLeaders.get(5).getPlayer().getUrl(), "l/lopezbr01");
		assertEquals(reboundingLeaders.get(5).getRebounds(), 2);
		
		assertEquals(reboundingLeaders.get(6).getQuarter(), "Tot");
		assertEquals(reboundingLeaders.get(6).getPlayer().getUrl(), "a/antetgi01");
		assertEquals(reboundingLeaders.get(6).getRebounds(), 23);
	}
	
	@Test
	void testGetReboundingLeadersHamcrest() {
		List<ReboundingLeader> reboundingLeaders = calculator.getLineScore().getReboundingLeaders();
		assertThat(reboundingLeaders.get(0), allOf(
                hasProperty("quarter", is("1st Q")),
                hasProperty("playerUrl", is("s/siakapa01")),
                hasProperty("rebounds", is(5))
        ));
		assertThat(reboundingLeaders.get(1), allOf(
                hasProperty("quarter", is("2nd Q")),
                hasProperty("playerUrl", is("2 tied")),
                hasProperty("rebounds", is(3))
        ));
		assertThat(reboundingLeaders.get(2), allOf(
                hasProperty("quarter", is("3rd Q")),
                hasProperty("playerUrl", is("a/antetgi01")),
                hasProperty("rebounds", is(7))
        ));
		assertThat(reboundingLeaders.get(3), allOf(
                hasProperty("quarter", is("4th Q")),
                hasProperty("playerUrl", is("a/antetgi01")),
                hasProperty("rebounds", is(6))
        ));
		assertThat(reboundingLeaders.get(4), allOf(
                hasProperty("quarter", is("1st OT")),
                hasProperty("playerUrl", is("a/antetgi01")),
                hasProperty("rebounds", is(3))
        ));
		assertThat(reboundingLeaders.get(5), allOf(
                hasProperty("quarter", is("2nd OT")),
                hasProperty("playerUrl", is("l/lopezbr01")),
                hasProperty("rebounds", is(2))
        ));
		assertThat(reboundingLeaders.get(6), allOf(
                hasProperty("quarter", is("Tot")),
                hasProperty("playerUrl", is("a/antetgi01")),
                hasProperty("rebounds", is(23))
        ));
	}
	
	@Test
	void testGetAssistLeaders() {
		List<AssistLeader> assistLeaders = calculator.getLineScore().getAssistLeaders();
		assertEquals(7, assistLeaders.size());
		
		assertEquals(assistLeaders.get(0).getQuarter(), "1st Q");
		assertEquals(assistLeaders.get(0).getPlayer().getUrl(), "g/gasolma01");
		assertEquals(assistLeaders.get(0).getAssists(), 4);
		
		assertEquals(assistLeaders.get(1).getQuarter(), "2nd Q");
		assertEquals(assistLeaders.get(1).getPlayer().getUrl(), "l/leonaka01");
		assertEquals(assistLeaders.get(1).getAssists(), 4);
		
		assertEquals(assistLeaders.get(2).getQuarter(), "3rd Q");
		assertEquals(assistLeaders.get(2).getPlayer().getUrl(), "2 tied");
		assertEquals(assistLeaders.get(2).getAssists(), 2);
		
		assertEquals(assistLeaders.get(3).getQuarter(), "4th Q");
		assertEquals(assistLeaders.get(3).getPlayer().getUrl(), "b/bledser01");
		assertEquals(assistLeaders.get(3).getAssists(), 2);
		
		assertEquals(assistLeaders.get(4).getQuarter(), "1st OT");
		assertEquals(assistLeaders.get(4).getPlayer().getUrl(), "a/antetgi01");
		assertEquals(assistLeaders.get(4).getAssists(), 2);
		
		assertEquals(assistLeaders.get(5).getQuarter(), "2nd OT");
		assertEquals(assistLeaders.get(5).getPlayer().getUrl(), "v/vanvlfr01");
		assertEquals(assistLeaders.get(5).getAssists(), 1);
		
		assertEquals(assistLeaders.get(6).getQuarter(), "Tot");
		assertEquals(assistLeaders.get(6).getPlayer().getUrl(), "2 tied");
		assertEquals(assistLeaders.get(6).getAssists(), 7);
	}
	
	@Test
	void testGetAssistLeadersHamcrest() {
		List<AssistLeader> assistLeaders = calculator.getLineScore().getAssistLeaders();
		assertThat(assistLeaders.get(0), allOf(
                hasProperty("quarter", is("1st Q")),
                hasProperty("playerUrl", is("g/gasolma01")),
                hasProperty("assists", is(4))
        ));
		assertThat(assistLeaders.get(1), allOf(
                hasProperty("quarter", is("2st Q")),
                hasProperty("playerUrl", is("l/leonaka01")),
                hasProperty("assists", is(4))
        ));
		assertThat(assistLeaders.get(2), allOf(
                hasProperty("quarter", is("3st Q")),
                hasProperty("playerUrl", is("2 tied")),
                hasProperty("assists", is(2))
        ));
		assertThat(assistLeaders.get(3), allOf(
                hasProperty("quarter", is("4st Q")),
                hasProperty("playerUrl", is("b/bledser01")),
                hasProperty("assists", is(2))
        ));
		assertThat(assistLeaders.get(4), allOf(
                hasProperty("quarter", is("1st OT")),
                hasProperty("playerUrl", is("a/antetgi01")),
                hasProperty("assists", is(2))
        ));
		assertThat(assistLeaders.get(5), allOf(
                hasProperty("quarter", is("2nd OT")),
                hasProperty("playerUrl", is("v/vanvlfr01")),
                hasProperty("assists", is(1))
        ));
		assertThat(assistLeaders.get(6), allOf(
                hasProperty("quarter", is("Tot")),
                hasProperty("playerUrl", is("2 tied")),
                hasProperty("assists", is(7))
        ));
	}

}