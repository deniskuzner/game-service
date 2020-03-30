package com.mozzartbet.gameservice.message;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mozzartbet.gameservice.domain.playbyplay.Foul;
import com.mozzartbet.gameservice.domain.playbyplay.Miss;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.domain.playbyplay.Point;
import com.mozzartbet.gameservice.domain.playbyplay.Rebound;
import com.mozzartbet.gameservice.domain.playbyplay.Substitution;
import com.mozzartbet.gameservice.domain.playbyplay.Turnover;

@Component
public class PlayByPlayActionMessageConverter implements Converter<PlayByPlayAction, PlayByPlayActionMessage> {

	@Override
	public PlayByPlayActionMessage convert(PlayByPlayAction action) {
		if (action instanceof Point) {
			return new PointMessageConverter().convert((Point) action);
		}
		if(action instanceof Rebound) {
			return new ReboundMessageConverter().convert((Rebound) action);
		}
		if(action instanceof Foul) {
			return new FoulMessageConverter().convert((Foul) action);
		}
		if(action instanceof Miss) {
			return new MissMessageConverter().convert((Miss) action);
		}
		if(action instanceof Substitution) {
			return new SubstitutionMessageConverter().convert((Substitution) action);
		}
		if(action instanceof Turnover) {
			return new TurnoverMessageConverter().convert((Turnover) action);
		}

		throw new IllegalArgumentException("Play by play action type not supported: " + action);
	}

	static abstract class BasePlayByPlayActionMessageConverter<A extends PlayByPlayAction, M extends PlayByPlayActionMessage>
			implements Converter<A, M> {

		@Override
		public final M convert(A a) {
			M m = create();
			populate(m, a);
			return m;
		}

		protected void populate(M m, A a) {
			m.setId(a.getId());
			m.setMatchId(a.getMatchId());
			m.setTeamId(a.getTeam().getId());
			m.setTimestamp(a.getTimestamp());
			m.setSumScore(a.getSumScore());
			m.setQuarter(a.getQuarter());
		}

		protected abstract M create();

	}

	static class PointMessageConverter extends BasePlayByPlayActionMessageConverter<Point, PointMessage> {
		@Override
		protected PointMessage create() {
			return new PointMessage();
		}

		@Override
		protected void populate(PointMessage m, Point p) {
			super.populate(m, p);
			m.setPoints(p.getPoints());
			m.setPointPlayerId(p.getPointPlayer().getId());
			m.setAssistPlayerId(p.getAssistPlayer().getId());
		}

	}

	static class ReboundMessageConverter extends BasePlayByPlayActionMessageConverter<Rebound, ReboundMessage> {
		@Override
		protected ReboundMessage create() {
			return new ReboundMessage();
		}

		@Override
		protected void populate(ReboundMessage m, Rebound r) {
			super.populate(m, r);
			m.setPlayerId(r.getPlayer().getId());
		}
	}
	
	static class FoulMessageConverter extends BasePlayByPlayActionMessageConverter<Foul, FoulMessage> {
		@Override
		protected FoulMessage create() {
			return new FoulMessage();
		}

		@Override
		protected void populate(FoulMessage m, Foul f) {
			super.populate(m, f);
			m.setPlayerId(f.getFoulByPlayer().getId());
		}
	}
	
	static class MissMessageConverter extends BasePlayByPlayActionMessageConverter<Miss, MissMessage> {
		@Override
		protected MissMessage create() {
			return new MissMessage();
		}

		@Override
		protected void populate(MissMessage m, Miss miss) {
			super.populate(m, miss);
			m.setMissPlayerId(miss.getMissPlayer().getId());
			m.setBlockPlayerId(miss.getBlockPlayer().getId());
		}
	}
	
	static class SubstitutionMessageConverter extends BasePlayByPlayActionMessageConverter<Substitution, SubstitutionMessage> {
		@Override
		protected SubstitutionMessage create() {
			return new SubstitutionMessage();
		}

		@Override
		protected void populate(SubstitutionMessage m, Substitution s) {
			super.populate(m, s);
			m.setInPlayerId(s.getInPlayer().getId());
			m.setOutPlayerId(s.getOutPlayer().getId());
		}
	}
	
	static class TurnoverMessageConverter extends BasePlayByPlayActionMessageConverter<Turnover, TurnoverMessage> {
		@Override
		protected TurnoverMessage create() {
			return new TurnoverMessage();
		}

		@Override
		protected void populate(TurnoverMessage m, Turnover t) {
			super.populate(m, t);
			m.setTurnoverPlayerId(t.getTurnoverPlayer().getId());
			m.setStealPlayerId(t.getStealPlayer().getId());
		}
	}
	
}
