package com.grijesh.cricket.controller;

import com.grijesh.cricket.dto.ApiError;
import com.grijesh.cricket.dto.Player;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(PlayerController.class)
public class PlayerControllerTest {

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

        verify(playerService).addPlayer(playerArgumentCaptor.capture());
        assertThat(playerArgumentCaptor.getValue().name()).isEqualTo("Grijesh");
        assertThat(playerArgumentCaptor.getValue().shortName()).isEqualTo("Big G");
        assertThat(playerArgumentCaptor.getValue().role()).isEqualTo("ADMIN");
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
    @DisplayName("Get Player API should return player details")
    public void getPlayerShouldReturnPlayerDetails() {
        when(playerService.getPlayer(1L)).thenReturn(new Player(1L, "Test", "short", "ADMIN"));

        webTestClient
                .get()
                .uri("/api/players/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo("Test")
                .jsonPath("$.shortName").isEqualTo("short")
                .jsonPath("$.role").isEqualTo("ADMIN");

    }

    @Test
    @DisplayName("Get Player API should return NOT FOUND when player not found")
    public void getPlayerShouldThrowNotFoundWhenPlayerNotFound() {
        when(playerService.getPlayer(1L)).thenThrow(new PlayerNotFoundException("Player Not Found"));

        webTestClient
                .get()
                .uri("/api/players/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .value(ApiError::status, CoreMatchers.equalTo(404))
                .value(ApiError::reason, CoreMatchers.equalTo("Player Not Found"));

    }

}
