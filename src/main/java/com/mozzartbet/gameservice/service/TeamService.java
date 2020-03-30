package com.mozzartbet.gameservice.service;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.mozzartbet.gameservice.domain.Team;

public interface TeamService {

	List<Team> getAll();

	Team getById(@NotNull @Min(1) Long id);
	
	Team getByUrl(String url);
	
	Team save(Team team);

	int deleteById(@NotNull @Min(1) Long id);
	
}
