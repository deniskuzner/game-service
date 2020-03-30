package com.mozzartbet.gameservice.parser;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.exception.HtmlParserException;

public class SeasonsPageHtmlParser {

	private Document document;
	private static final String SEASON_URL = "https://www.basketball-reference.com/leagues/";

	public SeasonsPageHtmlParser() {
		try {
			String link = String.format(SEASON_URL);
			document = Jsoup.connect(link).get();
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			throw new HtmlParserException("Page could not be found: " + SEASON_URL, e);
		}
	}

	public List<Season> parse() {
		List<Season> seasons = new ArrayList<>();

		Element table = document.getElementById("all_stats");
		Elements rows = table.getElementsByTag("tr");

		for (Element row : rows) {
			if (row.className().contains("thead"))
				continue;

			int year = Integer.parseInt(row.getElementsByTag("th").get(0).text().substring(0, 4)) + 1;
			String league = row.getElementsByTag("td").get(0).text();


			seasons.add(Season.builder().year(year).league(league).build());
		}

		return seasons;
	}
}