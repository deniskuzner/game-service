package com.mozzartbet.gameservice.stats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.statistics.LineScore;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.domain.statistics.linescore.AssistLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ReboundingLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LineScoreCalculator {

	private Match match;
	private List<PlayerStats> playersStats;
	private List<String> pbpQuarters;

	public LineScoreCalculator(Match match, List<PlayerStats> playersStats) {
		this.match = match;
		this.playersStats = playersStats;
		this.pbpQuarters = match.getPbpActions().stream().map(a -> a.getQuarter()).distinct().collect(Collectors.toList());
	}

	public LineScore getLineScore() {
		List<ScoringQuarter> scoringQuarters = getScoringQuarters();
		List<ScoringLeader> scoringLeaders = getScoringLeaders();
		List<ReboundingLeader> reboundingLeaders = getReboundingLeaders();
		List<AssistLeader> assistLeaders = getAssistLeaders();

		return LineScore.builder().matchUrl(match.getUrl()).scoringQuarters(scoringQuarters).scoringLeaders(scoringLeaders).reboundingLeaders(reboundingLeaders).assistLeaders(assistLeaders).build();
	}

	//Broj poena po cetvrtinama
	private List<ScoringQuarter> getScoringQuarters() {
		List<ScoringQuarter> scoringQuarters = new ArrayList<>();
		
		int hostPointsTotal = 0;
		int guestPointsTotal = 0;
		
		for (String quarter : pbpQuarters) {
			List<Point> points = match.getPbpActions().stream().filter(action -> action.isOfTypeQuarter(Point.class, quarter)).map(action -> (Point) action).collect(Collectors.toList());
			
			int hostPoints = points.stream().filter(action -> action.isHost(match, action.getTeam())).mapToInt(action -> action.getPoints()).sum();
			int guestPoints = points.stream().filter(action -> action.isGuest(match, action.getTeam())).mapToInt(action -> action.getPoints()).sum();
			
			hostPointsTotal += hostPoints;
			guestPointsTotal += guestPoints;
			
			scoringQuarters.add(ScoringQuarter.builder().match(match).quarter(quarter).hostPoints(hostPoints).guestPoints(guestPoints).build());
		}
		
		scoringQuarters.add(ScoringQuarter.builder().quarter("T").hostPoints(hostPointsTotal).guestPoints(guestPointsTotal).build());
		
		for (ScoringQuarter quarter : scoringQuarters) {
			System.out.println(quarter.getQuarter() + " " + quarter.getHostPoints() + " " + quarter.getGuestPoints());
		}
		
		return scoringQuarters;
	}

	//Lista najboljih strelaca po cetvrtinama
	private List<ScoringLeader> getScoringLeaders() {
		
		List<ScoringLeader> scoringLeaders = new ArrayList<>();
		List<Player> players = getPlayers();
		
		//Za svaku cetvrtinu uzimam listu strelaca
		for (String quarter : pbpQuarters) {
			List<ScoringLeader> quarterScoringLeaders = new ArrayList<>();
			
			for(Player player : players) {
				//Svi poeni zadate cetvrtine
				List<Point> points = match.getPbpActions().stream().filter(action -> action.isOfTypeQuarter(Point.class, quarter)).map(action -> (Point) action).collect(Collectors.toList());
				//Svi poeni zadatog igraca u zadatoj cetvrtini
				int playerPoints = points.stream().filter(action -> action.isExpectedPlayer(action.getPointPlayer(), player)).mapToInt(action -> action.getPoints()).sum();
				
				quarterScoringLeaders.add(ScoringLeader.builder().quarter(quarter).player(player).points(playerPoints).build());
			}
			
			//Sortirana lista strelaca po cetvrtini
			List<ScoringLeader> sortedQuarterScoringLeaders = quarterScoringLeaders.stream().sorted(Comparator.comparing(ScoringLeader::getPoints).reversed()).collect(Collectors.toList());
			
			//Proveravamo da li vise igraca deli prvo mesto
			for (ScoringLeader scoringLeader : sortedQuarterScoringLeaders) {
				if(scoringLeader.getPoints() == sortedQuarterScoringLeaders.get(0).getPoints())
					scoringLeaders.add(scoringLeader);
			}
		}
		
		//Najbolji strelac na utakmici
		PlayerStats maxPointsPlayer = playersStats.stream().max(Comparator.comparing(PlayerStats::getPoints)).orElseThrow(NoSuchElementException::new);
		for (PlayerStats playerStats : playersStats) {
			if(playerStats.getPoints() == maxPointsPlayer.getPoints()) 
				scoringLeaders.add(ScoringLeader.builder().quarter("Tot").player(playerStats.getPlayer()).points(playerStats.getPoints()).build());
		}
		
		System.out.println("POENI:");
		for (ScoringLeader player : scoringLeaders) {
			System.out.println(player.getQuarter() + " " + player.getPlayer().getUrl() + " " + player.getPoints());
		}
		
		return Collections.unmodifiableList(scoringLeaders);
	}

	private List<ReboundingLeader> getReboundingLeaders() {
		List<ReboundingLeader> reboundingLeaders = new ArrayList<>();
		List<Player> players = getPlayers();
		
		//Za svaku cetvrtinu uzimam listu skakaca
		for (String quarter : pbpQuarters) {
			List<ReboundingLeader> quarterReboundingLeaders = new ArrayList<>();
			
			for(Player player : players) {
				//Svi skokovi zadate cetvrtine
				List<Rebound> rebounds = match.getPbpActions().stream().filter(action -> action.isOfTypeQuarter(Rebound.class, quarter)).map(action -> (Rebound) action).collect(Collectors.toList());
				//Svi skokovi zadatog igraca u zadatoj cetvrtini
				int playerRebounds = rebounds.stream().filter(action -> action.isExpectedPlayer(action.getPlayer(), player)).collect(Collectors.toList()).size();
				
				quarterReboundingLeaders.add(ReboundingLeader.builder().quarter(quarter).player(player).rebounds(playerRebounds).build());
			}
			
			//Sortirana lista skakaca po cetvrtini
			List<ReboundingLeader> sortedQuarterReboundingLeaders = quarterReboundingLeaders.stream().sorted(Comparator.comparing(ReboundingLeader::getRebounds).reversed()).collect(Collectors.toList());
			
			//Proveravamo da li vise igraca deli prvo mesto
			for (ReboundingLeader player : sortedQuarterReboundingLeaders) {
				if(player.getRebounds() == sortedQuarterReboundingLeaders.get(0).getRebounds())
					reboundingLeaders.add(player);
			}
			
		}
		//Najbolji skakac na utakmici
		PlayerStats maxReboundsPlayer = playersStats.stream().max(Comparator.comparing(PlayerStats::getTotalRebounds)).orElseThrow(NoSuchElementException::new);
		for (PlayerStats playerStats : playersStats) {
			if(playerStats.getTotalRebounds() == maxReboundsPlayer.getTotalRebounds())
				reboundingLeaders.add(ReboundingLeader.builder().quarter("Tot").player(playerStats.getPlayer()).rebounds(playerStats.getTotalRebounds()).build());
		}
		
		System.out.println("SKOKOVI:");
		for (ReboundingLeader player : reboundingLeaders) {
			System.out.println(player.getQuarter() + " " + player.getPlayer().getUrl() + " " + player.getRebounds());
		}
		
		
		return Collections.unmodifiableList(reboundingLeaders);
	}

	private List<AssistLeader> getAssistLeaders() {
		List<AssistLeader> assistLeaders = new ArrayList<>();
		List<Player> players = getPlayers();
		
		//Za svaku cetvrtinu uzimam listu asistenata
		for (String quarter : pbpQuarters) {
			List<AssistLeader> quarterAssistLeaders = new ArrayList<>();
			
			for(Player player : players) {
				//Sve asistencije zadate cetvrtine
				List<Point> points = match.getPbpActions().stream().filter(action -> action.isOfTypeQuarter(Point.class, quarter)).map(action -> (Point) action).collect(Collectors.toList());
				//Sve asistencije zadatog igraca u zadatoj cetvrtini
				int playerAssists = points.stream().filter(action -> action.isExpectedPlayer(action.getAssistPlayer(), player)).collect(Collectors.toList()).size();
				
				quarterAssistLeaders.add(AssistLeader.builder().quarter(quarter).player(player).assists(playerAssists).build());
			}
			
			//Sortirana lista asistenata po cetvrtini
			List<AssistLeader> sortedQuarterAssistLeaders = quarterAssistLeaders.stream().sorted(Comparator.comparing(AssistLeader::getAssists).reversed()).collect(Collectors.toList());
			
			//Proveravamo da li vise igraca deli prvo mesto
			for (AssistLeader assistLeader : sortedQuarterAssistLeaders) {
				if(assistLeader.getAssists() == sortedQuarterAssistLeaders.get(0).getAssists())
					assistLeaders.add(assistLeader);
			}
			
		}
		//Najbolji asistent na utakmici
		PlayerStats maxAssistPlayer = playersStats.stream().max(Comparator.comparing(PlayerStats::getAssists)).orElseThrow(NoSuchElementException::new);
		for (PlayerStats playerStats : playersStats) {
			if(playerStats.getAssists() == maxAssistPlayer.getAssists())
				assistLeaders.add(AssistLeader.builder().quarter("Tot").player(playerStats.getPlayer()).assists(playerStats.getAssists()).build());
		}

		System.out.println("ASISTENCIJE:");
		for (AssistLeader player : assistLeaders) {
			System.out.println(player.getQuarter() + " " + player.getPlayer().getUrl() + " " + player.getAssists());
		}
		
		return Collections.unmodifiableList(assistLeaders);
	}
	
	
	//Lista igraca koji su bili na toj utakmici
	private List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		
		try {
			Document document = Jsoup.connect("https://www.basketball-reference.com/boxscores/"+match.getUrl()+".html").get();
			
			Elements hostRows = document.getElementsByClass("table_outer_container").get(0).getElementsByTag("tr");
			for (Element element : hostRows) {
				if(element.getClass().equals("thead") ||  element.getElementsByTag("a").size() == 0)
					continue;
				Player player = Player.builder()
						.url(element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf(".")))
						.team(match.getHost())
						.name(element.getElementsByTag("a").get(0).text()).build();
				players.add(player);
			}
			
			Elements guestRows = document.getElementsByClass("table_outer_container").get(2).getElementsByTag("tr");
			for (Element element : guestRows) {
				if(element.getClass().equals("thead") ||  element.getElementsByTag("a").size() == 0)
					continue;
				Player player = Player.builder()
						.url(element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf(".")))
						.team(match.getGuest())
						.name(element.getElementsByTag("a").get(0).text()).build();
				players.add(player);
			}
				
		} catch (IOException e) {
			System.out.println("Page could not be found.");
			e.printStackTrace();
		}
			
		return Collections.unmodifiableList(players);
	}
	
}

