package com.mozzartbet.gameservice.parser;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.exception.HtmlParserException;

public class SeasonMatchPageHtmlParser {

	private Document document;
	private static final String SEASON_URL = "https://www.basketball-reference.com/leagues/%s_games.html";

	public SeasonMatchPageHtmlParser(String season) {
		try {
			String link = String.format(SEASON_URL, season);
			document = Jsoup.connect(link).get();
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			throw new HtmlParserException("Page could not be found: " + season, e);
		}
	}

	public List<Match> parse() {
		List<Match> matches = new ArrayList<>();
		List<String> allMatchesIds = getAllMatchesIds();
		
		for (String matchId : allMatchesIds) {
			  MatchPageHtmlParser parser = new MatchPageHtmlParser(matchId);
			  matches.add(parser.parse());
		}
		
		return Collections.unmodifiableList(matches);
	}

	public List<String> getAllMatchesIds() {
		List<String> allMatchesIds = new ArrayList<>();

		List<Document> monthPages = getMonthPages();

		for (Document monthPage : monthPages) {
			Element schedule = monthPage.getElementById("schedule");
			Elements rows = schedule.getElementsByTag("tr");
			rows.remove(0);

			for (Element row : rows) {
				if(row.className().equals("thead"))
					continue;
				
				allMatchesIds.add(row.getElementsByTag("a").get(3).attr("href").substring(11,23));
			}
		}

		return allMatchesIds;
	}

	public List<Document> getMonthPages() {
		List<Document> monthPages = new ArrayList<>();

		List<String> monthUrls = getMonthUrls();

		for (String monthUrl : monthUrls) {
			try {
				monthPages.add(Jsoup.connect("https://www.basketball-reference.com" + monthUrl).get());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return monthPages;
	}

	public List<String> getMonthUrls() {

		List<String> monthUrls = new ArrayList<>();

		Element content = document.getElementsByClass("filter").get(0);
		Elements months = content.getElementsByTag("a");

		for (Element element : months) {
			monthUrls.add(element.attr("href"));
		}

		return monthUrls;
	}

}
