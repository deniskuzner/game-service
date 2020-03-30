package com.mozzartbet.gameservice.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class PlayerPageHtmlParserTest {

	private Document document;
	private PlayerPageHtmlParser playerParserUrl;
	private PlayerPageHtmlParser playerParserFile;
	private Element meta;
	private Elements spans;
	private Element content;
	private Elements rows;

	@Before
	public void getDocument() {
		try {
			document = Jsoup.parse(new File("src/test/resources/com/mozzartbet/gameservice/parser/"
					+ "2018-19 Milwaukee Bucks Roster and Stats _ Basketball-Reference.com.html"), "UTF-8", "");
			playerParserUrl = new PlayerPageHtmlParser("MIL/2019");
			playerParserFile = new PlayerPageHtmlParser(new File("src/test/resources/com/mozzartbet/gameservice/parser/"
							+ "2018-19 Milwaukee Bucks Roster and Stats _ Basketball-Reference.com.html"));
			meta = document.getElementById("meta");
			spans = meta.getElementsByTag("span");
			content = document.getElementById("roster");
			rows = content.getElementsByTag("tr");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testParseHtmlPage() {
		assertNotNull(document);
	}

	@Test
	public void testGetTeamName() {
		assertEquals("Milwaukee Bucks", spans.get(1).text());
	}
	
	@Test
	public void testGetPlayerId() {
		assertEquals("b/bledser01", rows.get(1).getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").substring(9, rows.get(1).getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").indexOf(".")));
	}

	@Test
	public void testGetPlayerNumber() {
		assertEquals(6, Integer.parseInt(rows.get(1).getElementsByTag("th").get(0).text()));
	}

	@Test
	public void testGetPlayerName() {
		assertEquals("Eric Bledsoe", rows.get(1).getElementsByTag("td").get(0).text());
	}

	@Test
	public void testGetPlayerPosition() {
		assertEquals("PG", rows.get(1).getElementsByTag("td").get(1).text());
	}

	@Test
	public void testGetPlayerHeight() {
		assertEquals("6-1", rows.get(1).getElementsByTag("td").get(2).text());
	}

	@Test
	public void testGetPlayerWeight() {
		assertEquals(205, Integer.parseInt(rows.get(1).getElementsByTag("td").get(3).text()));
	}

	@Test
	public void testGetPlayerBirthDate() {
		assertEquals("December 9, 1989", rows.get(1).getElementsByTag("td").get(4).text());
	}

	@Test
	public void testGetPlayerExperience() {
		assertEquals("8", rows.get(1).getElementsByTag("td").get(6).text());
	}

	@Test
	public void testGetPlayerCollege() {
		assertEquals("University of Kentucky", rows.get(1).getElementsByTag("td").get(7).text());
	}

	@Test
	public void playersShouldntBeEmpty() {
		assertFalse(playerParserFile.parse().isEmpty());
	}

	@Test
	public void teamShouldContainSixteenPlayers() {
		assertThat(playerParserFile.parse().size() == 16, is(true));
	}

	@Test(timeout = 500)
	public void parsingFromUrlTimeout() throws InterruptedException {
		Thread.sleep(1000);
		playerParserUrl.parse();
	}
	
}
