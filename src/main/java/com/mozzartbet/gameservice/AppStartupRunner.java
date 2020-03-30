package com.mozzartbet.gameservice;

import java.io.File;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.mozzartbet.gameservice.parser.MatchPageHtmlParser;
import com.mozzartbet.gameservice.parser.PlayerPageHtmlParser;
import com.mozzartbet.gameservice.parser.SeasonMatchPageHtmlParser;
import com.mozzartbet.gameservice.parser.SeasonPlayerPageHtmlParser;
import com.mozzartbet.gameservice.parser.SeasonsPageHtmlParser;
import com.mozzartbet.gameservice.parser.SingleSeasonParser;

@Component
public class AppStartupRunner implements ApplicationRunner {

	// private MatchPageHtmlParser matchParser = new
	// MatchPageHtmlParser("201905190TOR");
	// private MatchPageHtmlParser matchParser = new
	// MatchPageHtmlParser("201905160GSW");
	private MatchPageHtmlParser matchParser = new MatchPageHtmlParser("201810160BOS");
	private PlayerPageHtmlParser playerParserUrl = new PlayerPageHtmlParser("MIL/2019");
	private SeasonPlayerPageHtmlParser seasonPlayerParser = new SeasonPlayerPageHtmlParser("NBA_2018");
	private SeasonMatchPageHtmlParser seasonMatchParser = new SeasonMatchPageHtmlParser("NBA_2017");
	private PlayerPageHtmlParser playerParserFile = new PlayerPageHtmlParser(new File("src/test/resources/com/mozzartbet/gameservice/parser/2018-19 Milwaukee Bucks Roster and Stats _ Basketball-Reference.com.html"));
	private MatchPageHtmlParser matchParserFile = new MatchPageHtmlParser(new File("src/test/resources/com/mozzartbet/gameservice/parser/Milwaukee Bucks at Toronto Raptors Play-By-Play, May 19, 2019 _ Basketball-Reference.com.html"));
	private SingleSeasonParser singleSeasonParser = new SingleSeasonParser("NBA_2007");
	private SeasonsPageHtmlParser SeasonMatchPageHtmlParser = new SeasonsPageHtmlParser();
	//private MatchStatsCalculator statsCalculator = new MatchStatsCalculator(matchParser.parse());

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//matchParser.parse();
		// playerParserUrl.parse();
		// seasonPlayerParser.parse();
		// seasonMatchParser.parse();
		// playerParserFile.parse();
		// matchParserFile.parse();
		//statsCalculator.getMatchStats();
		// singleSeasonParser.parse();
		
	}

}
