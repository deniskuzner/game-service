package com.mozzartbet.gameservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.service.PlayerService;

@RestController
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	@RequestMapping("/players")
	public List<Player> getAll() {
		return playerService.getAll();
	}
	
	@RequestMapping("/player")
	public Player getById(@RequestParam Long id) {
		return playerService.getById(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/players")
	public void save(@RequestBody Player player) {
		playerService.save(player);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/players")
	public int delete(@RequestParam Long id) {
		return playerService.deleteById(id);
	}
}
