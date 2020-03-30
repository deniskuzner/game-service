package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;

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
public class Player implements BaseEntity {

	private Long id;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	private String url;
	private String number;
	private String name;
	private String position;
	private String height;
	private int weight;
	private String birthDate;
	private String experience;
	private String college;
	private Team team;

}
