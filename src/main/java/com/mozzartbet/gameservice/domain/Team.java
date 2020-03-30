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
public class Team implements BaseEntity {

	private Long id;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	private String url;
	private String name;

}
