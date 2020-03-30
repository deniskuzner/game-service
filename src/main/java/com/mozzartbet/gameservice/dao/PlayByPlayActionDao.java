package com.mozzartbet.gameservice.dao;

import java.util.List;

import com.mozzartbet.gameservice.domain.playbyplay.PlayByPlayAction;
import com.mozzartbet.gameservice.service.dto.FindPbpActionsRequest;

public interface PlayByPlayActionDao {

	 List<PlayByPlayAction> findPbpActions(FindPbpActionsRequest request, int sent);
	 
	 int updateSent(Long id);
		
	 long countSent(int sent);
	 
}
