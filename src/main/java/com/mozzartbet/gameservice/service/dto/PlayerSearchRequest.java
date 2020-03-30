package com.mozzartbet.gameservice.service.dto;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PlayerSearchRequest {

	@Pattern(regexp = "\\w+")
	String playerName;

	@Min(1)
	Long teamId;
	
	Set<String> positions;
	
	@Pattern(regexp = "\\w+")
	String college;
	
	@Pattern(regexp = "\\d{1,2}")
	String number;

}