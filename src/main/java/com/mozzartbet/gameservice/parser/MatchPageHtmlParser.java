package com.mozzartbet.gameservice.parser;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.QuarterInfo;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;
import com.mozzartbet.gameservice.exception.HtmlParserException;

public class MatchPageHtmlParser {

	private Document document;
	private String matchUrl;
	private static final String MATCH_URL = "https://www.basketball-reference.com/boxscores/pbp/%s.html";

	// Parsiranje iz lokalnog fajla
	public MatchPageHtmlParser(File input) {
		try {
			document = Jsoup.parse(input, "UTF-8", "");
			matchUrl = document.getElementsByClass("filter").get(0).getElementsByTag("a").get(0).attr("href")
					.substring(11, document.getElementsByClass("filter").get(0).getElementsByTag("a").get(0).attr("href").indexOf("."));
		} catch (IOException e) {
			throw new HtmlParserException("File could not be found: " + input.getAbsolutePath(), e);
		}
	}
	
	// Parsiranje sa sajta
	public MatchPageHtmlParser(String matchUrl) {
		try {
			String link = String.format(MATCH_URL, matchUrl);
			document = Jsoup.connect(link).get();
			this.matchUrl = matchUrl;
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			throw new HtmlParserException("Page could not be found: " + matchUrl, e);
		}
	}

	public Match parse() {

		Element content = document.getElementById("pbp");
		Elements rows = content.getElementsByTag("tr");
		Elements scorebox = document.getElementsByClass("scorebox");
		String seasonUrl = document.getElementsByClass("hoversmooth").get(1).getElementsByTag("a").get(1).attr("href");

		List<PlayByPlayAction> actions = new ArrayList<>();

		String quarter = "";

		// Uzimanje timova, ukupnog rezultata i sezone
		Team host = Team.builder().url(scorebox.get(0).getElementsByTag("strong").get(0).getElementsByTag("a").get(0).attr("href").substring(7,15)).name(scorebox.get(0).getElementsByTag("strong").get(0).text()).build();
		Team guest = Team.builder().url(scorebox.get(0).getElementsByTag("strong").get(1).getElementsByTag("a").get(0).attr("href").substring(7,15)).name(scorebox.get(0).getElementsByTag("strong").get(1).text()).build();
		String result = document.getElementsByClass("score").get(0).text() + "-" + document.getElementsByClass("score").get(1).text();
		Season season = Season.builder().league(seasonUrl.substring(9,12)).year(Integer.parseInt(seasonUrl.substring(13,17))).build();
		
		System.out.println("\n\nSEASON: " + season.getYear() + " MATCH: " + host.getName() + " - " + guest.getName() + "\n\n");
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:m:s.S");
		
		for (Element element : rows) {

			PlayByPlayAction action = null;

			// Provera da li je u pitanju red koji oznacava cetvrtinu
			if (element.className().equals("thead") && Character.isDigit(element.text().charAt(0))) {
				System.out.println(element.text());
				quarter = element.text();
				continue;
			}

			// Provera da li je u pitanju zaglavlje tabele
			if (element.className().equals("thead") && !Character.isDigit(element.text().charAt(0))) {
				continue;
			}

			// Provera da li je u pitanju pocetni ili krajnji dogadjaj za cetvrtinu
			if (element.getElementsByTag("td").size() == 2) {
				
				System.out.println(element.getElementsByTag("td").get(0).text() + " " + element.getElementsByTag("td").get(1).text());
				continue;
			}

			// Provera da li je: faul, poen, promasaj, rebound, izmena, timeout, turnover
			if (element.getElementsByTag("td").size() == 6) {
				int descriptionIndex = -1;
				
				LocalTime timestamp = LocalTime.parse("0:"+element.getElementsByTag("td").get(0).text(), dateTimeFormatter);
				
				String sumScore = element.getElementsByTag("td").get(3).text();
				Team team = null;

				// Test da li je prvi ili drugi tim u pitanju
				if (element.getElementsByTag("td").get(1).text().length() > 1) {
					debugFirstTeam(element, quarter);
					descriptionIndex = 1;
					team = host;

				} else {
					debugSecondTeam(element, quarter);
					descriptionIndex = 5;
					team = guest;
				}

				String description = element.getElementsByTag("td").get(descriptionIndex).text();

				// Provera da li je poentirao
				if (description.contains("makes")) {
					action = getPoint(element, quarter, description, timestamp, sumScore, team);
				}

				// Provera da li je faul
				if (description.contains(" foul ")) {
					action = getFoul(element, quarter, description, timestamp, sumScore, team);
				}

				// Provera da li je promasaj
				if (description.contains(" misses ")) {
					action = getMiss(element, quarter, timestamp, description, team, sumScore);
				}

				// Provera da li je rebound
				if (description.contains(" rebound by ")) {
					action = getRebound(element, quarter, timestamp, description, team, sumScore);
				}

				// Provera da li je izmena
				if (description.contains(" enters the game ")) {
					action = getSubstitution(element, quarter, timestamp, description, team, sumScore);
				}

				// Provera da li je timeout
				if (description.contains(" timeout")) {
					continue;
				}
				
				// Provera da li je turnover
				if (description.contains("Turnover by")) {
					action = getTurnover(element, quarter, timestamp, description, team, sumScore);
				}
				
			}

			if (element.getElementsByTag("td").size() == 6 && element.getElementsByTag("a").size() == 0)
				continue;
			
			if(action == null)
				continue;

			actions.add(action);
		}
		
		Match match = Match.builder().url(matchUrl).host(host).guest(guest).result(result).pbpActions(actions).season(season).build();

		return match;
	}

	private Point getPoint(Element element, String quarter, String description, LocalTime timestamp, String sumScore, Team team) {
		int points = 0;
		if(description.contains("free throw"))
			points = 1;
		if(description.contains("2-pt"))
			points = 2;
		if(description.contains("3-pt"))
			points = 3;
		
		String pointPlayerUrl = element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf("."));

		String assistPlayerUrl = "";
		// Provera da li postoji asistent
		if (element.getElementsByTag("a").size() > 1)
			assistPlayerUrl = element.getElementsByTag("a").get(1).attr("href").substring(9, element.getElementsByTag("a").get(1).attr("href").indexOf("."));

		Player pointPlayer = Player.builder().url(pointPlayerUrl).build();
		Player assistPlayer = Player.builder().url(assistPlayerUrl).build();
		return Point.builder().quarter(quarter).timestamp(timestamp).description(description).points(points).team(team).sumScore(sumScore).pointPlayer(pointPlayer).assistPlayer(assistPlayer).build();
	}

	private PlayByPlayAction getFoul(Element element, String quarter, String description, LocalTime timestamp, String sumScore, Team team) {
		String foulByUrl = "";
		if (element.getElementsByTag("a").size() > 0)
			foulByUrl = element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf("."));
		
		Player foulByPlayer = Player.builder().url(foulByUrl).build();
		return Foul.builder().quarter(quarter).timestamp(timestamp).description(description).team(team).sumScore(sumScore).foulByPlayer(foulByPlayer).build();
	}
	
	private PlayByPlayAction getMiss(Element element, String quarter, LocalTime timestamp, String description, Team team, String sumScore) {
		String missPlayerUrl = "";
		String blockPlayerUrl = "";

		if (element.getElementsByTag("a").size() > 0)
			missPlayerUrl = element.getElementsByTag("a").get(0).attr("href").substring(9,
					element.getElementsByTag("a").get(0).attr("href").indexOf("."));
		if (element.getElementsByTag("a").size() > 1)
			blockPlayerUrl = element.getElementsByTag("a").get(1).attr("href").substring(9,
					element.getElementsByTag("a").get(1).attr("href").indexOf("."));

		Player missPlayer = Player.builder().url(missPlayerUrl).build();
		Player blockPlayer = Player.builder().url(blockPlayerUrl).build();
		return Miss.builder().quarter(quarter).timestamp(timestamp).description(description).team(team).sumScore(sumScore).missPlayer(missPlayer).blockPlayer(blockPlayer).build();
	}
	
	private PlayByPlayAction getRebound(Element element, String quarter, LocalTime timestamp, String description, Team team, String sumScore) {
		String playerUrl = "";
		if (element.getElementsByTag("a").size() > 0)
			playerUrl = element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf("."));

		Player player = Player.builder().url(playerUrl).build();
		return Rebound.builder().quarter(quarter).timestamp(timestamp).description(description).team(team).sumScore(sumScore).player(player).build();
	}
	
	private PlayByPlayAction getSubstitution(Element element, String quarter, LocalTime timestamp, String description, Team team, String sumScore) {
		String inPlayerUrl = "";
		String outPlayerUrl = "";
		if (element.getElementsByTag("a").size() > 0)
			inPlayerUrl = element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf("."));
		if (element.getElementsByTag("a").size() > 1)
			outPlayerUrl = element.getElementsByTag("a").get(1).attr("href").substring(9, element.getElementsByTag("a").get(1).attr("href").indexOf("."));
		
		Player inPlayer = Player.builder().url(inPlayerUrl).build();
		Player outPlayer = Player.builder().url(outPlayerUrl).build();
		return Substitution.builder().quarter(quarter).timestamp(timestamp).description(description).team(team).sumScore(sumScore).inPlayer(inPlayer).outPlayer(outPlayer).build();
	}
	
	private PlayByPlayAction getTurnover(Element element, String quarter, LocalTime timestamp, String description, Team team, String sumScore) {
		String turnoverPlayerUrl = "";
		String stealPlayerUrl = "";

		if (element.getElementsByTag("a").size() > 0) 
			turnoverPlayerUrl = element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf("."));
		
		if (element.getElementsByTag("a").size() > 1) 
			stealPlayerUrl = element.getElementsByTag("a").get(1).attr("href").substring(9, element.getElementsByTag("a").get(1).attr("href").indexOf("."));

		Player turnoverPlayer = Player.builder().url(turnoverPlayerUrl).build();
		Player stealPlayer = Player.builder().url(stealPlayerUrl).build();
		return Turnover.builder().quarter(quarter).timestamp(timestamp).description(description).team(team).sumScore(sumScore).turnoverPlayer(turnoverPlayer).stealPlayer(stealPlayer).build();
	}
	
	private void debugFirstTeam(Element element, String quarter) {
		System.out.println(quarter + " " + element.getElementsByTag("td").get(0).text() + " "
				+ element.getElementsByTag("td").get(1).text() + " "
				+ element.getElementsByTag("td").get(2).text() + " "
				+ element.getElementsByTag("td").get(3).text());
	}
	
	private void debugSecondTeam(Element element, String quarter) {
		System.out.println(quarter + " " + element.getElementsByTag("td").get(0).text() + " "
				+ element.getElementsByTag("td").get(5).text() + " "
				+ element.getElementsByTag("td").get(4).text() + " "
				+ element.getElementsByTag("td").get(3).text());
	}
	
}
