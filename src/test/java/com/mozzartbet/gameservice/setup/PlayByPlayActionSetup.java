package com.mozzartbet.gameservice.setup;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;
import com.mozzartbet.gameservice.mapper.PlayByPlayActionMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlayByPlayActionSetup {

	@Autowired
	PlayByPlayActionMapper pbpActionMapper;
	
	
	public List<PlayByPlayAction> getSetup(List<Player> ps){
		
		Player p1 = ps.get(0);
		Player p2 = ps.get(1);
		Team t = p1.getTeam();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:m:s.S");

		log.info("Saving actions");
		List<PlayByPlayAction> actions = new ArrayList<PlayByPlayAction>();
		actions.add(Point.builder().matchId(1L).timestamp(LocalTime.parse("00:12:00.0", dateTimeFormatter)).description("description").quarter("1st Q")
				.sumScore("2:0").team(t).points(2).pointPlayer(p1).assistPlayer(p2).build());
		actions.add(Rebound.builder().matchId(1L).player(p2).description("description").timestamp(LocalTime.parse("00:11:55.0", dateTimeFormatter))
				.sumScore("2:3").quarter("1st Q").team(t).build());
		actions.add(Foul.builder().matchId(1L).timestamp(LocalTime.parse("00:11:50.0", dateTimeFormatter)).description("foul description").quarter("1st Q")
				.sumScore("5:3").foulByPlayer(p1).team(t).build());
		actions.add(Miss.builder().matchId(2L).timestamp(LocalTime.parse("00:04:04.0", dateTimeFormatter)).description("miss description").quarter("3rd Q")
				.sumScore("21:45").missPlayer(p1).blockPlayer(p2).team(t).build());
		actions.add(Substitution.builder().matchId(2L).timestamp(LocalTime.parse("00:07:34.0", dateTimeFormatter)).description("substitution description")
				.quarter("3rd Q").sumScore("44:56").inPlayer(p1).outPlayer(p2).team(t).build());
		actions.add(Turnover.builder().matchId(2L).timestamp(LocalTime.parse("00:02:35.0", dateTimeFormatter)).description("turnover description")
				.quarter("4th Q").sumScore("48:67").turnoverPlayer(p1).stealPlayer(p2).team(t).build());

		actions.forEach(a -> {
			assertEquals(pbpActionMapper.insert(a), 1);
			assertEquals(pbpActionMapper.insertSpec(a), 1);
		});
		
		return actions;
	}
	
	public long countSent(int sent) {
		return pbpActionMapper.countSent(sent);
	}
}
