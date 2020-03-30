package com.mozzartbet.gameservice.parser;

import java.io.File;
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
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.HtmlParserException;


public class PlayerPageHtmlParser {

	private Document document;
	private static final String TEAM_URL = "https://www.basketball-reference.com/teams/%s.html";
	
	// Parsiranje iz lokalnog fajla
	public PlayerPageHtmlParser(File input) {
		try {
			document = Jsoup.parse(input, "UTF-8", "");
		} catch (IOException e) {
			throw new HtmlParserException("File could not be found: " + input.getAbsolutePath(), e);
		}
	}

	// Parsiranje sa sajta
	public PlayerPageHtmlParser(String teamId) {
		try {
			String link = String.format(TEAM_URL, teamId);
			document = Jsoup.connect(link).get();
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			throw new HtmlParserException("Page could not be found: " + teamId, e);
		}
	}
	
	public List<Player> parse() {
		// tabela roster bez zaglavlja
		Element content = document.getElementById("roster");
		Elements rows = content.getElementsByTag("tr");
		rows.remove(0);

		// ime tima
		Element meta = document.getElementById("meta");
		Elements spans = meta.getElementsByTag("span");
		Team team = Team.builder().url(document.getElementsByClass("in_list").get(0).getElementsByTag("a").get(0).attr("href").substring(7,15)).name(spans.get(1).text()).build();
		
		List<Player> players = new ArrayList<Player>();

		for (Element element : rows) {

			String url = element.getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").indexOf("."));
			String number = element.getElementsByTag("th").get(0).text();
			String name = element.getElementsByTag("td").get(0).text();
			String position = element.getElementsByTag("td").get(1).text();
			String height = element.getElementsByTag("td").get(2).text();
			int weight = Integer.parseInt(element.getElementsByTag("td").get(3).text());
			String birthDate = element.getElementsByTag("td").get(4).text();
			String experience = element.getElementsByTag("td").get(6).text();
			String college = element.getElementsByTag("td").get(7).text();

			System.out.println(number + " " + name + " " + position + " " + height + " " + weight + " " + birthDate
					+ " " + experience + " " + college);
			
			players.add(Player.builder().url(url).number(number).name(name).position(position).height(height).weight(weight).birthDate(birthDate).experience(experience).college(college).team(team).build());
		}

		return Collections.unmodifiableList(players);
	}

}
