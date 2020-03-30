package com.mozzartbet.gameservice.domain.statistics;

import java.util.List;

import com.mozzartbet.gameservice.domain.statistics.linescore.AssistLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ReboundingLeader;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringQuarter;
import com.mozzartbet.gameservice.domain.statistics.linescore.ScoringLeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class LineScore {
	private String matchUrl;
	private List<ScoringQuarter> scoringQuarters;
	private List<ScoringLeader> scoringLeaders;
	private List<ReboundingLeader> reboundingLeaders;
	private List<AssistLeader> assistLeaders;

}
