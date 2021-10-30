package com.grijesh.cricket.service;

import com.grijesh.cricket.dto.Player;
import com.grijesh.cricket.dto.Roles;
import com.grijesh.cricket.exception.DuplicatePlayerException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String NAME = "Test";
    private static final String SHORT_NAME = "Short";

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Captor
    private ArgumentCaptor<PlayerEntity> playerEntityArgumentCaptor;

    @Mock
    private PlayerEntity mockPlayerEntity;


    @Test
    @DisplayName("Create player should covert player into entity and send to repository")
    public void addPlayerShouldSendPlayerEntityToDb() {
        when(playerRepository.save(playerEntityArgumentCaptor.capture())).thenReturn(mockPlayerEntity);

        assertThatCode(() -> playerService.createPlayer(new Player(1L, NAME, SHORT_NAME, Roles.ADMIN)))
                .doesNotThrowAnyException();

        PlayerEntity playerEntity = playerEntityArgumentCaptor.getValue();

        assertThat(playerEntity.getId()).isEqualTo(null);
        assertThat(playerEntity.getName()).isEqualTo(NAME);
        assertThat(playerEntity.getShortName()).isEqualTo(SHORT_NAME);
        assertThat(playerEntity.getRole()).isEqualTo(ADMIN_ROLE);
    }

    @Test
    @DisplayName("Create player should throw an exception if player with same name already present")
    public void addPlayerShouldThrowExceptionIfPlayerAlreadyPresent() {
        when(mockPlayerEntity.getName()).thenReturn(NAME);
        when(playerRepository.findByName(NAME)).thenReturn(Optional.of(mockPlayerEntity));

        assertThatThrownBy(() -> playerService.createPlayer(new Player(1L, NAME, SHORT_NAME, Roles.ADMIN)))
                .isInstanceOf(DuplicatePlayerException.class)
                .hasMessage("Player with name [Test] is already present.");

        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    @DisplayName("Get Player should return player details")
    public void getPlayerShouldReturnPlayerFromRepo() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(mockPlayerEntity));
        when(mockPlayerEntity.getId()).thenReturn(1L);
        when(mockPlayerEntity.getName()).thenReturn(NAME);
        when(mockPlayerEntity.getShortName()).thenReturn(SHORT_NAME);
        when(mockPlayerEntity.getRole()).thenReturn(ADMIN_ROLE);

        Player player = playerService.getPlayer(1L);

        assertThat(player.id()).isEqualTo(1L);
        assertThat(player.name()).isEqualTo(NAME);
        assertThat(player.shortName()).isEqualTo(SHORT_NAME);
        assertThat(player.role()).isEqualTo(Roles.ADMIN);
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

    @Test
    @DisplayName("Get players should return all the players")
    public void getPlayersShouldReturnAllPlayers() {
        when(mockPlayerEntity.getId()).thenReturn(1L).thenReturn(2L);
        when(mockPlayerEntity.getName()).thenReturn(NAME).thenReturn("Test2");
        when(mockPlayerEntity.getShortName()).thenReturn(SHORT_NAME).thenReturn("Short2");
        when(mockPlayerEntity.getRole()).thenReturn(ADMIN_ROLE).thenReturn("PLAYER");
        when(playerRepository.findAll()).thenReturn(List.of(mockPlayerEntity, mockPlayerEntity));

        List<Player> players = playerService.getPlayers();

        assertThat(players.size()).isEqualTo(2);

        assertThat(players.get(0).id()).isEqualTo(1L);
        assertThat(players.get(0).name()).isEqualTo(NAME);
        assertThat(players.get(0).shortName()).isEqualTo(SHORT_NAME);
        assertThat(players.get(0).role()).isEqualTo(Roles.ADMIN);

        assertThat(players.get(1).id()).isEqualTo(2L);
        assertThat(players.get(1).name()).isEqualTo("Test2");
        assertThat(players.get(1).shortName()).isEqualTo("Short2");
        assertThat(players.get(1).role()).isEqualTo(Roles.PLAYER);
    }

}