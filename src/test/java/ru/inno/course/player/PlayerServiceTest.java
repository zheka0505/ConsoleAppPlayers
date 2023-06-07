package ru.inno.course.player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.inno.course.player.extensions.FileHelperExt;
import ru.inno.course.player.extensions.HtmlReporter;
import ru.inno.course.player.extensions.MyTestWatcher;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Тесты на бизнес-логику")
@ExtendWith(FileHelperExt.class)
@ExtendWith(MyTestWatcher.class)
@ExtendWith(HtmlReporter.class)
public class PlayerServiceTest {
    private PlayerService service;
    private final static String PLAYER_NAME = "Player name";

    @BeforeEach
    public void setUp() {
        service = new PlayerServiceImpl();
    }

    @DisplayName("Создание пользователя с валидными данными")
    @ParameterizedTest(name = "Имя игрока = {0}")
    @MethodSource("getPlayers")
    public void a_shouldCreatePlayer(Player p) {
        int newId = service.createPlayer(p.getNick());
        int size = service.getPlayers().size();
        Player player = service.getPlayerById(newId);

        assertEquals(1, newId);
        assertEquals(1, size);
        assertEquals(p.getNick(), player.getNick());
    }
    public static Player[] getPlayers(){
        return new Player[]{
                new Player(1, "1", 1, true),
                new Player(2, "2", 2, true),
                new Player(3, "3", 3, true),
        };
    }

    public static String[] getPlayerNames() {
        // read file
        // HTTP GET-request
        // SELECT from DB
        // RANDOM Generator
        return new String[]{"12345", "abcde", "русский игрок", "_____Player1+++", "@@@@@"};
    }

    @DisplayName("Игрок с невалидным ником `null`. Игрок не создается.")
    @ParameterizedTest(name = "{index} => Создаем игрока с ником {0}")
    @ValueSource(strings = {" "})
    @NullAndEmptySource // @NullSource + @EmptySource
    public void shouldNotCreatePlayerWithNullNick(String name) {
        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(name));
        assertEquals(0, service.getPlayers().size());
    }

    @Test
    @DisplayName("Игрок с таким именем уже существует. Игрок не создается.")
    public void shouldNotCreatePlayerNickAlreadyInUse() {
        int newId = service.createPlayer(PLAYER_NAME);
        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(PLAYER_NAME));
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

    @DisplayName("Добавление очков игроку. Негативные тесты")
    @ParameterizedTest(name = "{index} => Добавление очков игроку {0}")
    @ValueSource(ints = {0, -10, -100, -1, Integer.MIN_VALUE})
    public void testPointsAddingParams(int points) {
        int newId = service.createPlayer(PLAYER_NAME);
        assertThrows(IllegalArgumentException.class, () -> service.addPoints(newId, points));
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
        assertThrows(NoSuchElementException.class, () -> service.deletePlayer(newId + 1));
    }
}
