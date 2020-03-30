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

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.exception.HtmlParserException;

public class SeasonPlayerPageHtmlParser {

	private Document document;
	private static final String SEASON_URL = "https://www.basketball-reference.com/leagues/%s.html";
	
	
	public SeasonPlayerPageHtmlParser(String season) {
		try {
			String link = String.format(SEASON_URL, season);
			document = Jsoup.connect(link).get();
			document = removeComments(document);
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			throw new HtmlParserException("Page could not be found: " + season, e);
		}
	}

	public List<Player> parse() {
		List<Player> players = new ArrayList<Player>();
		List<String> teams = getTeams();

		for (String string : teams) {
			PlayerPageHtmlParser parser = new PlayerPageHtmlParser(string);
			List<Player> teamPlayers = parser.parse();
			
			for (Player player : teamPlayers) {
				players.add(player);
			}
		}
		
		return Collections.unmodifiableList(players);
	}

	public List<String> getTeams() {

		List<String> teams = new ArrayList<>();

		Element content = document.getElementById("div_team-stats-per_game");
		Elements rows = content.getElementsByTag("tr");
		rows.remove(0);
		rows.remove(rows.size() - 1);

		for (Element element : rows) {
			teams.add(element.getElementsByTag("a").get(0).attr("href").substring(7,15));
		}
		
		return teams;

	}

	public Document removeComments(Document document) {
		String docString = document.outerHtml().replace("<!--", "").replace("-->", "");
		return Jsoup.parse(docString);
	}
}
