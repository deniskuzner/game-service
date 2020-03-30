package com.mozzartbet.gameservice.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mozzartbet.gameservice.domain.Season;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SeasonMapperTest extends BaseMapperTest {

	@Autowired
	SeasonMapper seasonMapper;
	
	@Test
	void testCrud() {
		
		log.info("Insert season");
		
		Season s1 = Season.builder().year(2019).league("NBA").build();
		Season s2 = Season.builder().year(1971).league("ABA").build();
		Season s3 = Season.builder().year(1971).league("NBA").build();
		
		assertEquals(seasonMapper.insert(s1), 1);
		assertEquals(seasonMapper.insert(s2), 1);
		assertEquals(seasonMapper.insert(s3), 1);
		
		log.info("Get all");
		assertEquals(seasonMapper.getAll().size(), 3);
		
		log.info("Get by id");
		assertEquals(seasonMapper.getById(s1.getId()).getYear(), 2019);
		assertEquals(seasonMapper.getById(s1.getId()).getLeague(), "NBA");
		
		log.info("Get by league");
		assertEquals(seasonMapper.getByYear(1971, "NBA").getId(), s3.getId());
		assertEquals(seasonMapper.getByYear(1971, "NBA").getLeague(), "NBA");
		assertEquals(seasonMapper.getByYear(1971, "NBA").getYear(), 1971);
		
		log.info("Update");
		assertEquals(seasonMapper.update(Season.builder().id(s2.getId()).year(2018).league("NBA").build()), 1);
		assertEquals(seasonMapper.getById(s2.getId()).getYear(), 2018);
		assertEquals(seasonMapper.getById(s2.getId()).getLeague(), "NBA");
		
		log.info("Delete");
		assertEquals(seasonMapper.deleteById(s1.getId()), 1);
		assertEquals(seasonMapper.deleteById(s2.getId()), 1);
		assertEquals(seasonMapper.deleteById(s3.getId()), 1);
		assertEquals(seasonMapper.getAll().size(), 0);
	}

}