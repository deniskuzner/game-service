package com.mozzartbet.gameservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mozzartbet.gameservice.domain.Player;

public final class PlayerAssert {
	
	private PlayerAssert() {}
	
	public static void assertPlayer(Player actual, Player expected) {
		assertEquals(actual.getName(), expected.getName());
		assertEquals(actual.getNumber(), expected.getNumber());
		assertEquals(actual.getPosition(), expected.getPosition());
		assertEquals(actual.getHeight(), expected.getHeight());
		assertEquals(actual.getWeight(), expected.getWeight());
		assertEquals(actual.getBirthDate(), expected.getBirthDate());
		assertEquals(actual.getExperience(), expected.getExperience());
		assertEquals(actual.getCollege(), expected.getCollege());
		assertNotNull(actual.getTeam());
		assertEquals(actual.getTeam().getId(), expected.getTeam().getId());
		if(expected.getTeam().getName() != null)
			assertEquals(actual.getTeam().getName(), expected.getTeam().getName());
		if(expected.getTeam().getUrl() != null)
			assertEquals(actual.getTeam().getUrl(), expected.getTeam().getUrl());
	}

}
