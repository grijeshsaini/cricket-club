package com.grijesh.cricket;

import com.grijesh.cricket.dto.Roles;
import com.grijesh.cricket.persistence.PlayerEntity;
import com.grijesh.cricket.persistence.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;

public class CricketClubIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("Should able to create player.")
    public void shouldAbleToCreatePlayer() {
        final String request = """
                {
                    "name" : "Grijesh",
                    "shortName" : "Big G",
                    "role" : "ADMIN"
                }
                """;

        webTestClient
                .post()
                .uri("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();

        Optional<PlayerEntity> player = playerRepository.findByName("Grijesh");
        assertThat(player.isPresent()).isTrue();
        assertThat(player.get().getRole()).isEqualTo(Roles.ADMIN.name());
        assertThat(player.get().getShortName()).isEqualTo("Big G");
    }

    @Test
    @DisplayName("Should able to fetch player from Database.")
    public void shouldAbleToFetchPlayer() {
        PlayerEntity playerEntity = playerRepository.save(new PlayerEntity("Test", "Short", "ADMIN"));

        webTestClient
                .get()
                .uri("/api/players/{id}", playerEntity.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(playerEntity.getId())
                .jsonPath("$.name").isEqualTo(playerEntity.getName())
                .jsonPath("$.shortName").isEqualTo(playerEntity.getShortName())
                .jsonPath("$.role").isEqualTo(playerEntity.getRole());
    }

    @Test
    @DisplayName("Should able to fetch all players from Database.")
    public void shouldAbleToFetchPlayers() {
        PlayerEntity playerEntity1 = playerRepository.save(new PlayerEntity("AllPlayer1", "AllPlayer1-Short", "ADMIN"));
        PlayerEntity playerEntity2 = playerRepository.save(new PlayerEntity("AllPlayer2", "AllPlayer1-Short", "PLAYER"));

        webTestClient
                .get()
                .uri("/api/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[*].id").value(hasItems(playerEntity1.getId().intValue(), playerEntity2.getId().intValue()))
                .jsonPath("$[*].name").value(hasItems(playerEntity1.getName(), playerEntity2.getName()))
                .jsonPath("$[*].shortName").value(hasItems(playerEntity1.getShortName(), playerEntity2.getShortName()))
                .jsonPath("$[*].role").value(hasItems(Roles.ADMIN.name(), Roles.PLAYER.name()));
    }
}
