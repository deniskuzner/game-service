package com.mozzartbet.gameservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;

@Repository
public interface PlayByPlayActionRepository extends MongoRepository<PlayByPlayAction, String>, PlayByPlayActionCustomRepository {

}
