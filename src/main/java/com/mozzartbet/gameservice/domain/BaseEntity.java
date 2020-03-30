package com.mozzartbet.gameservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface BaseEntity extends Serializable {
	
	public Long getId();

	public void setId(Long id);

	public LocalDateTime getCreatedOn();

	public void setCreatedOn(LocalDateTime createdOn);

	public LocalDateTime getModifiedOn();

	public void setModifiedOn(LocalDateTime modifiedOn);
	
}
