package com.mozzartbet.gameservice.stats;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;
import com.mozzartbet.gameservice.domain.statistics.PlayerStats;
import com.mozzartbet.gameservice.exception.HtmlParserException;

public class PlayerStatsCalculator {

	private Match match;

	public PlayerStatsCalculator(Match match) {
		this.match = match;
	}

	public List<PlayerStats> getPlayerStats() {
		
		List<PlayerStats> playersStats = new ArrayList<>();

		//Uzimam sve igrace sa utakmice
		List<Player> players = getPlayers();
		
		//Kastovanje pbp akcija u konkretan dogadjaj
		List<Miss> missesFG = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Miss.class, "misses 2-pt")).map(action -> (Miss) action).collect(Collectors.toList());
		List<Miss> misses3P = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Miss.class, "misses 3-pt")).map(action -> (Miss) action).collect(Collectors.toList());
		List<Miss> missesFT = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Miss.class, "misses free throw")).map(action -> (Miss) action).collect(Collectors.toList());
		List<Foul> fouls = match.getPbpActions().stream().filter(action -> action instanceof Foul).map(action -> (Foul) action).collect(Collectors.toList());
		List<Point> points = match.getPbpActions().stream().filter(action -> action instanceof Point).map(action -> (Point) action).collect(Collectors.toList());
		List<Rebound> defensiveRebounds = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Rebound.class, "Defensive")).map(action -> (Rebound) action).collect(Collectors.toList());
		List<Rebound> offensiveRebounds = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Rebound.class, "Offensive")).map(action -> (Rebound) action).collect(Collectors.toList());
		List<Substitution> substitutions = match.getPbpActions().stream().filter(action -> action instanceof Substitution).map(action -> (Substitution) action).collect(Collectors.toList());
		List<Turnover> turnovers = match.getPbpActions().stream().filter(action -> action instanceof Turnover).map(action -> (Turnover) action).collect(Collectors.toList());
		List<Turnover> steals = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Turnover.class, "steal by")).map(action -> (Turnover) action).collect(Collectors.toList());
		List<Miss> blocks = match.getPbpActions().stream().filter(action -> action.isOfTypeDescription(Miss.class, "block by")).map(action -> (Miss) action).collect(Collectors.toList());

		//Uzimanje dogadjaja za konkretnog igraca
		for(Player player : players) {
			
			List<Miss> playerMissesFG = missesFG.stream().filter(action -> action.isExpectedPlayer(action.getMissPlayer(), player)).collect(Collectors.toList());
			List<Miss> playerMisses3P = misses3P.stream().filter(action -> action.isExpectedPlayer(action.getMissPlayer(), player)).collect(Collectors.toList());
			List<Miss> playerMissesFT = missesFT.stream().filter(action -> action.isExpectedPlayer(action.getMissPlayer(), player)).collect(Collectors.toList());
			List<Foul> playerFouls = fouls.stream().filter(action -> action.isExpectedPlayer(action.getFoulByPlayer(), player) && !action.getDescription().contains("Technical")).collect(Collectors.toList());
			List<Point> playerPointsFG = points.stream().filter(action -> action.isExpectedPlayer(action.getPointPlayer(), player) && action.getPoints() == 2).collect(Collectors.toList());
			List<Point> playerPoints3P = points.stream().filter(action -> action.isExpectedPlayer(action.getPointPlayer(), player) && action.getPoints() == 3).collect(Collectors.toList());
			List<Point> playerPointsFT = points.stream().filter(action -> action.isExpectedPlayer(action.getPointPlayer(), player) && action.getPoints() == 1).collect(Collectors.toList());
			List<Rebound> playerDefensiveRebounds = defensiveRebounds.stream().filter(action -> action.isExpectedPlayer(action.getPlayer(), player)).collect(Collectors.toList());
			List<Rebound> playerOffensiveRebounds = offensiveRebounds.stream().filter(action -> action.isExpectedPlayer(action.getPlayer(), player)).collect(Collectors.toList());
			List<Substitution> playerSubstitutions = substitutions.stream().filter(action -> action.isExpectedPlayer(action.getInPlayer(), player) || action.isExpectedPlayer(action.getOutPlayer(), player)).collect(Collectors.toList());
			List<Turnover> playerTurnovers = turnovers.stream().filter(action -> action.isExpectedPlayer(action.getTurnoverPlayer(), player)).collect(Collectors.toList());
			List<Turnover> playerSteals = steals.stream().filter(action -> action.isExpectedPlayer(action.getStealPlayer(), player)).collect(Collectors.toList());
			List<Point> playerAssists = points.stream().filter(action -> action.isExpectedPlayer(action.getAssistPlayer(), player)).collect(Collectors.toList());
			List<Miss> playerBlocks = blocks.stream().filter(action -> action.isExpectedPlayer(action.getBlockPlayer(), player)).collect(Collectors.toList());
			
			//Racunanje pojedinacnih statistika
			//FG
			int fieldGoals = playerPointsFG.size() + playerPoints3P.size();
			int fieldGoalAttempts = fieldGoals + playerMissesFG.size() + playerMisses3P.size();
			double fieldGoalPercentage = fieldGoalAttempts == 0 ? 0 : Math.round((double) fieldGoals/fieldGoalAttempts*1000.0)/1000.0;
			
			//3P
			int threePointFieldGoals = playerPoints3P.size();
			int threePointFieldGoalAttempts = playerPoints3P.size() + playerMisses3P.size();
			double threePointFieldGoalPercentage = threePointFieldGoalAttempts == 0 ? 0 : Math.round((double) threePointFieldGoals/threePointFieldGoalAttempts*1000.0)/1000.0;
			
			//FT
			int freeThrows = playerPointsFT.size();
			int freeThrowAttempts = playerPointsFT.size() + playerMissesFT.size();
			double freeThrowPercentage = freeThrowAttempts == 0 ? 0 : Math.round((double) freeThrows/freeThrowAttempts*1000.0)/1000.0;
			
			//Rebounds
			int offRebounds = playerOffensiveRebounds.size();
			int defRebounds = playerDefensiveRebounds.size();
			int totalRebounds = offRebounds + defRebounds;
			
			//Assists, steals, blocks, turnovers, fouls, points
			int assistsCount = playerAssists.size();
			int stealsCount = playerSteals.size();
			int blocksCount = playerBlocks.size();
			int turnoversCount = playerTurnovers.size();
			int foulsCount = playerFouls.size();
			int pointsCount = (fieldGoals - threePointFieldGoals)*2 + threePointFieldGoals*3 + freeThrows;
			
			//Izracunati MP i +/- !!!!
			
			
			System.out.println("url: " + player.getUrl() + "\tFG: " + fieldGoals + "\tFGA: " + fieldGoalAttempts +"\tFG%: "+ fieldGoalPercentage+ "\t3P: " + threePointFieldGoals + "\t3PA: " + threePointFieldGoalAttempts
					+ "\t3P%: " + threePointFieldGoalPercentage + "\tFT: " + freeThrows + "\tFTA: " + freeThrowAttempts + "\tFT%: " + freeThrowPercentage + "\tORB: " + offRebounds
					+ "\tDRB: " + defRebounds + "\tTRB: " + totalRebounds + "\tAST: " + assistsCount + "\tSTL: " + stealsCount + "\tBLK: " + blocksCount + "\tTOV: " + turnoversCount
					+ "\tPF: " + foulsCount + "\tPTS: " + pointsCount);

			PlayerStats playerStats = PlayerStats.builder().player(player).minutesPlayed(null).fieldGoals(fieldGoals).fieldGoalAttempts(fieldGoalAttempts).fieldGoalPercentage(fieldGoalPercentage)
					.threePointFieldGoals(threePointFieldGoals).threePointFieldGoalAttempts(threePointFieldGoalAttempts).threePointFieldGoalPercentage(threePointFieldGoalPercentage)
					.freeThrows(freeThrows).freeThrowAttempts(freeThrowAttempts).freeThrowPercentage(freeThrowPercentage)
					.offensiveRebounds(offRebounds).defensiveRebounds(defRebounds).totalRebounds(totalRebounds).assists(assistsCount).steals(stealsCount).blocks(blocksCount)
					.turnovers(turnoversCount).personalFouls(foulsCount).points(pointsCount).plusMinus(null).build();
			
			playersStats.add(playerStats);

		}
		
		return Collections.unmodifiableList(playersStats);
	}
	
	//Lista igraca koji su bili na toj utakmici
	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		
		try {
			Document document = Jsoup.connect("https://www.basketball-reference.com/boxscores/"+match.getUrl()+".html").get();
			
			Document hostRosterPage = Jsoup.connect("https://www.basketball-reference.com/teams/"+ match.getHost().getUrl() +".html").get();
			Elements hostRosterRows = hostRosterPage.getElementById("roster").getElementsByTag("tr");
			hostRosterRows.remove(0);
			List<String> hostRosterPlayerUrls = new ArrayList<>();
			for (Element element : hostRosterRows) {
				hostRosterPlayerUrls.add(element.getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").indexOf(".")));
			}
			
			Elements hostRows = document.getElementsByClass("table_outer_container").get(0).getElementsByTag("tr");
			for (Element element : hostRows) {
				if(element.getClass().equals("thead") ||  element.getElementsByTag("a").size() == 0)
					continue;
				
				Player player = Player.builder()
						.url(element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf(".")))
						.team(match.getHost()).build();
				
				//Ako je igrac bio na utakmici a nema ga na rosteru tima, izvlacim podatke sa njegove stranice
				if(!hostRosterPlayerUrls.contains(player.getUrl())) {
					setPlayerFields(player);
				}
				
				players.add(player);
			}
			
			Document guestRosterPage = Jsoup.connect("https://www.basketball-reference.com/teams/"+ match.getGuest().getUrl() +".html").get();
			Elements guestRosterRows = guestRosterPage.getElementById("roster").getElementsByTag("tr");
			guestRosterRows.remove(0);
			List<String> guestRosterPlayerUrls = new ArrayList<>();
			for (Element element : guestRosterRows) {
				guestRosterPlayerUrls.add(element.getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("td").get(0).getElementsByTag("a").get(0).attr("href").indexOf(".")));
			}
			
			Elements guestRows = document.getElementsByClass("table_outer_container").get(2).getElementsByTag("tr");
			for (Element element : guestRows) {
				if(element.getClass().equals("thead") ||  element.getElementsByTag("a").size() == 0)
					continue;
				Player player = Player.builder()
						.url(element.getElementsByTag("a").get(0).attr("href").substring(9, element.getElementsByTag("a").get(0).attr("href").indexOf(".")))
						.team(match.getGuest()).build();
				
				//Ako je igrac bio na utakmici a nema ga na rosteru tima, izvlacim podatke sa njegove stranice
				if(!guestRosterPlayerUrls.contains(player.getUrl())) {
					setPlayerFields(player);
				}
				
				players.add(player);
			}
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			System.out.println("Page could not be found.");
			e.printStackTrace();
		}
		
		return players;
	}

	// Parsiranje podataka za igrace kojih nema u rosteru
	private void setPlayerFields(Player player) {
		
		try {
			Document playerPage = Jsoup.connect("https://www.basketball-reference.com/players/"+ player.getUrl() +".html").get();
			Element playerInfo = playerPage.getElementById("info");
			player.setName(playerInfo.getElementsByTag("h1").text());
			
			String position = playerInfo.getElementsByTag("p").get(2).text();
			String p = " ";
			if(position.contains("Guard"))
				p = "G";
			if(position.contains("Shooting Guard"))
				p = "SG";
			if(position.contains("Point Guard"))
				p = "PG";
			if(position.contains("Center"))
				p = "C";
			if(position.contains("Power Forward"))
				p = "PF";
			if(position.contains("Small Forward"))
				p = "SF";
			player.setPosition(p);
			
			player.setHeight(playerInfo.getElementsByTag("span").get(0).text());
			player.setWeight(Integer.parseInt(playerInfo.getElementsByTag("span").get(1).text().substring(0,3)));
			player.setBirthDate(playerInfo.getElementsByTag("span").get(2).text());
			
		} catch (SocketTimeoutException ste) {
			System.out.println("Connect timed out!");
		} catch (IOException e) {
			throw new HtmlParserException("Page could not be found: " + player.getUrl(), e);
		}
	}

}
