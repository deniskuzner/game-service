package com.mozzartbet.gameservice.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mozzartbet.gameservice.dao.PlayByPlayActionDao;
import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.mapper.PlayByPlayActionMapper;
import com.mozzartbet.gameservice.service.dto.FindPbpActionsRequest;

@Repository
public class PlayByPlayActionDaoImpl implements PlayByPlayActionDao {

	@Autowired
	PlayByPlayActionMapper playByPlayActionMapper;

	@Override
	public List<PlayByPlayAction> findPbpActions(FindPbpActionsRequest request, int sent) {
		return playByPlayActionMapper.findPbpActions(request, sent);
	}

	@Override
	public int updateSent(Long id) {
		return playByPlayActionMapper.updateSent(id);
	}

	@Override
	public long countSent(int sent) {
		return playByPlayActionMapper.countSent(sent);
	}

}
