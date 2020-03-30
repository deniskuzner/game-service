package com.mozzartbet.gameservice.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class SeasonPlayerPageHtmlParserTest {

	private Document document;

	private SeasonPlayerPageHtmlParser parser = new SeasonPlayerPageHtmlParser("NBA_2018");


	@Before
	public void getDocument() {
		try {
			document = Jsoup.connect("https://www.basketball-reference.com/leagues/NBA_2018.html").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void parseHtmlPage() {
		assertNotNull(document);
	}
	
	@Test
	public void seasonShouldContainThirtyPlayers() {
		assertThat(parser.getTeams().size() == 30, is(true));
	}
	
}
