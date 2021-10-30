package com.grijesh.cricket.service;

import com.grijesh.cricket.dto.Player;
import com.grijesh.cricket.exception.PlayerNotFoundException;
import com.grijesh.cricket.persistence.PlayerEntity;
import com.grijesh.cricket.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Service to handle players.
 *
 * @author Grijesh Saini
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final Function<PlayerEntity, Player> entityToPlayer =
            (playerEntity -> new Player(playerEntity.getId(), playerEntity.getName(), playerEntity.getShortName(), playerEntity.getRole()));

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Create a new player.
     *
     * @param player an instance of {@link Player}.
     */
    public void addPlayer(Player player) {
        playerRepository.save(new PlayerEntity(player.name(), player.shortName(), player.role()));
    }

    /**
     * Fetch and returns Player for specific id.
     *
     * @return an instance of {@link Player} contains player details.
     */
    public Player getPlayer(Long id) {
        return playerRepository.findById(id)
                .map(entityToPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(String.format("Player not found with id [%d]", id)));
    }

}
