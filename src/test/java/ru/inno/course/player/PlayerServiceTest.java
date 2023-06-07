package ru.inno.course.player;

import org.junit.jupiter.api.*;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerServiceTest {
    private PlayerService service;
    private final static String PLAYER_NAME = "Player name";
    private static Path filePath;

    @BeforeEach
    public void setUp() {
        filePath = Path.of("data.json");
        service = new PlayerServiceImpl();
    }

    @AfterEach
    public void deleteFileIfNeeded() throws IOException {
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    public void shouldCreatePlayer() {
        int newId = service.createPlayer(PLAYER_NAME);
        int size = service.getPlayers().size();
        Player player = service.getPlayerById(newId);

        assertEquals(1, newId);
        assertEquals(1, size);
        assertEquals(PLAYER_NAME, player.getNick());
    }

    @Test
    @DisplayName("Игрок с ником `null`. Игрок не создается.")
    public void shouldNotCreatePlayerWithNullNick() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createPlayer(null));
        assertEquals(0, service.getPlayers().size());
    }

    @Test
    @DisplayName("Игрок с пустым ником \"\". Игрок не создается.")
    public void shouldNotCreatePlayerWithEmptyNick() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createPlayer(""));
        assertEquals(0, service.getPlayers().size());
    }


    @Test
    @DisplayName("Игрок с пустым ником \" \". Игрок не создается.")
    public void shouldNotCreatePlayerWithSpaceNick() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createPlayer(" "));
        assertEquals(0, service.getPlayers().size());
    }

    @Test
    @DisplayName("Игрок с таким именем уже существует. Игрок не создается.")
    public void shouldNotCreatePlayerNickAlreadyInUse() {
        int newId = service.createPlayer(PLAYER_NAME);
        assertThrows(IllegalArgumentException.class,
                () -> service.createPlayer(PLAYER_NAME));
        assertEquals(1, service.getPlayers().size());
        assertEquals(1, service.getPlayerById(newId).getId());
    }

    @Test
    @DisplayName("Можно создать удаленного игрока")
    public void shouldAllowRecreatePlayer() {
        int oldId = service.createPlayer(PLAYER_NAME);
        service.deletePlayer(oldId);
        int newId = service.createPlayer(PLAYER_NAME);

        assertEquals(1, oldId);
        assertEquals(2, newId);
        assertEquals(1, service.getPlayers().size());
    }

    @Test
    @DisplayName("Добавление очков игроку")
    public void shouldAddPointsToPlayer() {
        int newId = service.createPlayer(PLAYER_NAME);
        service.addPoints(newId, 10);
        assertEquals(10, service.getPlayerById(newId).getPoints());
        service.addPoints(newId, 10);
        assertEquals(20, service.getPlayerById(newId).getPoints());
    }

    @Test
    @DisplayName("Добавление очков игроку (отрицательное значение)")
    public void shouldNotAddNegativePoints() {
        int newId = service.createPlayer(PLAYER_NAME);
        assertThrows(IllegalArgumentException.class,
                () -> service.addPoints(newId, -10));
        assertEquals(0, service.getPlayerById(newId).getPoints());
    }

    @Test
    @DisplayName("Добавление очков игроку (0 очков)")
    public void shouldNotAddZeroPoints() {
        int newId = service.createPlayer(PLAYER_NAME);
        assertThrows(IllegalArgumentException.class,
                () -> service.addPoints(newId, 0));
        assertEquals(0, service.getPlayerById(newId).getPoints());
    }

    @Test
    @DisplayName("Удаление существующего пользователя")
    public void shouldDeletePlayer() {
        int newId = service.createPlayer(PLAYER_NAME);
        Player p = service.deletePlayer(newId);

        assertEquals(newId, p.getId());
        assertEquals(PLAYER_NAME, p.getNick());
        assertEquals(0, service.getPlayers().size());
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя")
    public void shouldNotDeleteUnknownPlayer() {
        int newId = service.createPlayer(PLAYER_NAME);
        assertThrows(NoSuchElementException.class,
                () -> service.deletePlayer(newId + 1));
    }
}
