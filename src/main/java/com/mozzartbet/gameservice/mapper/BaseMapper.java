package com.mozzartbet.gameservice.mapper;

import java.util.List;

import com.mozzartbet.gameservice.domain.BaseEntity;

public interface BaseMapper<E extends BaseEntity> {

	public long count();

	public List<E> getAll();
	
	public E getById(Long id);

	public int insert(E entity);

	public int update(E entity);

	public int deleteById(Long id);
	
	public default E save(E entity) {
		if (entity.getId() != null) {
			update(entity);
		}
		else {
			insert(entity);
		}
		return getById(entity.getId());
	}

}
