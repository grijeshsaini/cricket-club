package com.grijesh.cricket.controller;

import com.grijesh.cricket.dto.ApiError;
import com.grijesh.cricket.dto.Player;
import com.grijesh.cricket.dto.Roles;
import com.grijesh.cricket.exception.DuplicatePlayerException;
import com.grijesh.cricket.exception.PlayerNotFoundException;
import com.grijesh.cricket.service.PlayerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(PlayerController.class)
public class PlayerControllerTest {

    private static final String NAME = "Test";
    private static final String SHORT_NAME = "short";
    private static final long PLAYER_ID = 1L;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    @Captor
    private ArgumentCaptor<Player> playerArgumentCaptor;

    @Test
    @DisplayName("Create Player API should be able to create player")
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

        verify(playerService).createPlayer(playerArgumentCaptor.capture());
        assertThat(playerArgumentCaptor.getValue().name()).isEqualTo("Grijesh");
        assertThat(playerArgumentCaptor.getValue().shortName()).isEqualTo("Big G");
        assertThat(playerArgumentCaptor.getValue().role()).isEqualTo(Roles.ADMIN);
    }

    @Test
    @DisplayName("Create Player API should throw bad request when name is missing.")
    public void createPlayerShouldThrowBadRequestWhenNameIsMissing() {

        final String request = """
                {
                    "role" : "ADMIN"
                }
                """;

        webTestClient
                .post()
                .uri("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(playerService);
    }

    @Test
    @DisplayName("Create Player API should throw bad request when role is missing.")
    public void createPlayerShouldThrowBadRequestWhenRoleIsMissing() {

        final String request = """
                {
                    "name" : "G"
                }
                """;

        webTestClient
                .post()
                .uri("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(playerService);
    }

    @Test
    @DisplayName("Create Player API should throw bad request when invalid role is requested.")
    public void createPlayerShouldThrowBadRequestWhenInvalidRole() {

        final String request = """
                {
                    "name" : "G",
                    "role" : "BATSMAN"
                }
                """;

        webTestClient
                .post()
                .uri("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(playerService);
    }

    @Test
    @DisplayName("Create Player API should throw conflict when player with same name already present.")
    public void createPlayerShouldThrowConflictWhenPlayerAlreadyExist() {
        doThrow(new DuplicatePlayerException("Player with same name is already present")).when(playerService).createPlayer(any());

        final String request = """
                {
                    "name" : "G",
                    "role" : "PLAYER"
                }
                """;

        webTestClient
                .post()
                .uri("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ApiError.class)
                .value(ApiError::status, CoreMatchers.equalTo(409))
                .value(ApiError::reason, CoreMatchers.equalTo("Player with same name is already present"));
    }

    @Test
    @DisplayName("Get Player API should return player details")
    public void getPlayerShouldReturnPlayerDetails() {
        when(playerService.getPlayer(PLAYER_ID)).thenReturn(new Player(PLAYER_ID, NAME, SHORT_NAME, Roles.ADMIN));

        webTestClient
                .get()
                .uri("/api/players/{id}", PLAYER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.shortName").isEqualTo(SHORT_NAME)
                .jsonPath("$.role").isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Get Player API should return NOT FOUND when player not found")
    public void getPlayerShouldThrowNotFoundWhenPlayerNotFound() {
        when(playerService.getPlayer(PLAYER_ID)).thenThrow(new PlayerNotFoundException("Player Not Found"));

        webTestClient
                .get()
                .uri("/api/players/{id}", PLAYER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .value(ApiError::status, CoreMatchers.equalTo(404))
                .value(ApiError::reason, CoreMatchers.equalTo("Player Not Found"));
    }

    @Test
    @DisplayName("Get Players API should return all players")
    public void getPlayersShouldReturnAllPlayers() {
        Player player1 = new Player(PLAYER_ID, NAME, SHORT_NAME, Roles.ADMIN);
        Player player2 = new Player(2L, "Test2", "short2", Roles.PLAYER);
        when(playerService.getPlayers()).thenReturn(List.of(player1, player2));

        webTestClient
                .get()
                .uri("/api/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].name").isEqualTo(NAME)
                .jsonPath("$[0].shortName").isEqualTo(SHORT_NAME)
                .jsonPath("$[0].role").isEqualTo("ADMIN")
                .jsonPath("$[1].id").isEqualTo("2")
                .jsonPath("$[1].name").isEqualTo("Test2")
                .jsonPath("$[1].shortName").isEqualTo("short2")
                .jsonPath("$[1].role").isEqualTo("PLAYER");
    }
}
