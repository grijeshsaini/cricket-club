package com.grijesh.cricket.controller;

import com.grijesh.cricket.dto.Player;
import com.grijesh.cricket.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Rest Controller for handling players api's request
 */
@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(value = "/players", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlayer(@Valid @RequestBody Player player) {
        playerService.createPlayer(player);
    }

    @GetMapping(value = "/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Player getPlayer(@PathVariable("id") Long id) {
        return playerService.getPlayer(id);
    }

    @GetMapping(value = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Player> getPlayers() {
        return playerService.getPlayers();
    }
}
