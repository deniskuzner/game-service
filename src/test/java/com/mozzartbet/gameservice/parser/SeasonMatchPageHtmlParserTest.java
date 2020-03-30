package com.mozzartbet.gameservice.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class SeasonMatchPageHtmlParserTest {

	private Document document;
	private SeasonMatchPageHtmlParser parser;

	@Before
	public void getDocument() {
		try {
			document = Jsoup.connect("https://www.basketball-reference.com/leagues/NBA_2018_games.html").get();
			parser = new SeasonMatchPageHtmlParser("NBA_2018");
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
	public void seasonShouldContainNineMonths() {
		assertThat(parser.getMonthPages().size() == 9, is(true));
	}

}
