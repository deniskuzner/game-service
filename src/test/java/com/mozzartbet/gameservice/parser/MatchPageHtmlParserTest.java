package com.mozzartbet.gameservice.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class MatchPageHtmlParserTest {

	private Document document;
	private Element content;
	private Elements rows;
	private Elements scorebox;

	@Before
	public void getDocument() {
		try {
			document = Jsoup.parse(new File("src/test/resources/com/mozzartbet/gameservice/parser/"
					+ "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.html"), "UTF-8", "");
			content = document.getElementById("pbp");
			rows = content.getElementsByTag("tr");
			scorebox = document.getElementsByClass("scorebox");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseHtmlPage() {
		assertNotNull(document);
	}

	@Test
	public void testGetTeamName() {
		assertEquals("Portland Trail Blazers", scorebox.get(0).getElementsByTag("strong").get(0).text());
		assertEquals("Golden State Warriors", scorebox.get(0).getElementsByTag("strong").get(1).text());
	}

	@Test
	public void testGetTimestamp() {
		assertEquals("12:00.0", rows.get(2).getElementsByTag("td").get(0).text());
	}

	@Test
	public void testGetDescription() {
		assertEquals("Jump ball: E. Kanter vs. A. Bogut (S. Curry gains possession)",
				rows.get(2).getElementsByTag("td").get(1).text());
	}

	@Test
	public void testGetPoints() {
		assertEquals("2", rows.get(7).getElementsByTag("td").get(2).text().substring(1,2));
	}

	@Test
	public void testGetScore() {
		assertEquals("0-2", rows.get(3).getElementsByTag("td").get(3).text());
	}

	@Test
	public void testGetQuarterRow() {
		assertTrue(rows.get(0).className().equals("thead") && Character.isDigit(rows.get(0).text().charAt(0)));
	}
	
	@Test
	public void testGetQuarterInfoRow() {
		assertThat(rows.get(2).getElementsByTag("td").size() == 2, is(true));
	}
	
	@Test
	public void testGetFoulByPlayerId() {
		assertEquals(rows.get(4).getElementsByTag("a").get(0).attr("href").substring(9, rows.get(4).getElementsByTag("a").get(0).attr("href").indexOf(".")), "b/bogutan01");
	}
	
	@Test
	public void testGetFoulDrawnByPlayerId() {
		assertEquals(rows.get(4).getElementsByTag("a").get(1).attr("href").substring(9, rows.get(4).getElementsByTag("a").get(1).attr("href").indexOf(".")), "m/mccolcj01");
	}
	
	@Test
	public void testGetMissPlayerId() {
		assertEquals(rows.get(5).getElementsByTag("a").get(0).attr("href").substring(9, rows.get(5).getElementsByTag("a").get(0).attr("href").indexOf(".")), "a/aminual01");
	}

	@Test
	public void testGetActions() {
		MatchPageHtmlParser parser = new MatchPageHtmlParser(new File("src/test/resources/com/mozzartbet/gameservice/parser/"
				+ "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.html"));

		assertFalse(parser.parse().getPbpActions().isEmpty());
	}

}
