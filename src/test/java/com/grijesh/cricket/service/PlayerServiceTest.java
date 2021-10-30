package com.grijesh.cricket.service;

import com.grijesh.cricket.dto.Player;
import com.grijesh.cricket.exception.PlayerNotFoundException;
import com.grijesh.cricket.persistence.PlayerEntity;
import com.grijesh.cricket.persistence.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Captor
    private ArgumentCaptor<PlayerEntity> playerEntityArgumentCaptor;

    @Mock
    private PlayerEntity mockPlayerEntity;

    @Test
    @DisplayName("Add player should covert player into entity and send to repository")
    public void addPlayerShouldSendPlayerEntityToDb() {
        when(playerRepository.save(playerEntityArgumentCaptor.capture())).thenReturn(mockPlayerEntity);

        assertThatCode(() -> playerService.addPlayer(new Player(1L, "Test", "Short", "ADMIN")))
                .doesNotThrowAnyException();

        PlayerEntity playerEntity = playerEntityArgumentCaptor.getValue();

        assertThat(playerEntity.getId()).isEqualTo(null);
        assertThat(playerEntity.getName()).isEqualTo("Test");
        assertThat(playerEntity.getShortName()).isEqualTo("Short");
        assertThat(playerEntity.getRole()).isEqualTo("ADMIN");

    }

    @Test
    @DisplayName("Get Player should return player details")
    public void getPlayerShouldReturnPlayerFromRepo() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(mockPlayerEntity));
        when(mockPlayerEntity.getId()).thenReturn(1L);
        when(mockPlayerEntity.getName()).thenReturn("Test");
        when(mockPlayerEntity.getShortName()).thenReturn("Short");
        when(mockPlayerEntity.getRole()).thenReturn("ADMIN");

        Player player = playerService.getPlayer(1L);

        assertThat(player.id()).isEqualTo(1L);
        assertThat(player.name()).isEqualTo("Test");
        assertThat(player.shortName()).isEqualTo("Short");
        assertThat(player.role()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Get Player should return PlayerNotFoundException when player not found")
    public void getPlayerShouldThrowPlayerNotFoundException() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.getPlayer(1L))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage("Player not found with id [1]");

        verify(playerRepository).findById(1L);
    }

}