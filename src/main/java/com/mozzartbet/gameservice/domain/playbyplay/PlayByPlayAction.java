package com.mozzartbet.gameservice.domain.playbyplay;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mozzartbet.gameservice.domain.BaseEntity;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@SuperBuilder
@Document(collection = "play_by_play_actions")
public abstract class PlayByPlayAction implements BaseEntity{
	@Id
	private String mongo_id;
	
	private Long id;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	private Long matchId;
	private String quarter;
	private LocalTime timestamp;
	private String description;
	private String sumScore;
	private Team team;

	public <T extends PlayByPlayAction> boolean isOfTypeQuarter(Class<T> subclass, String quarter) {
		return subclass.isInstance(this) && this.quarter.equals(quarter);
	}
	
	public <T extends PlayByPlayAction> boolean isOfTypeDescription(Class<T> subclass, String description) {
		return subclass.isInstance(this) && this.description.contains(description);
	}


	public boolean isHost(Match match, Team team) {
		return team.getUrl().equals(match.getHost().getUrl());
	}

	public boolean isGuest(Match match, Team team) {
		return team.getUrl().equals(match.getGuest().getUrl());
	}
	
	public boolean isExpectedPlayer(Player actionPlayer, Player expectedPlayer) {
		if(actionPlayer == null)
			return false;
		return actionPlayer.getUrl().equals(expectedPlayer.getUrl());
	}
	
}
