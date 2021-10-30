package com.grijesh.cricket.service;

import com.grijesh.cricket.dto.Player;
import com.grijesh.cricket.dto.Roles;
import com.grijesh.cricket.exception.DuplicatePlayerException;
import com.grijesh.cricket.exception.PlayerNotFoundException;
import com.grijesh.cricket.persistence.PlayerEntity;
import com.grijesh.cricket.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service to handle players.
 *
 * @author Grijesh Saini
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final Function<PlayerEntity, Player> entityToPlayer =
            (playerEntity -> new Player(playerEntity.getId(), playerEntity.getName(), playerEntity.getShortName(), Roles.valueOf(playerEntity.getRole())));

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Create a new player.
     *
     * @param player an instance of {@link Player}.
     */
    public void createPlayer(Player player) {
        playerRepository.findByName(player.name())
                .ifPresentOrElse(p -> {
                    throw new DuplicatePlayerException(String.format("Player with name [%s] is already present.", p.getName()));
                }, () -> playerRepository.save(new PlayerEntity(player.name(), player.shortName(), player.role().name())));

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

    /**
     * Fetch and returns all players.
     *
     * @return a List of {@link Player} contains player details.
     */
    public List<Player> getPlayers() {
        return playerRepository.findAll().stream()
                .map(entityToPlayer)
                .collect(Collectors.toList());
    }

}
