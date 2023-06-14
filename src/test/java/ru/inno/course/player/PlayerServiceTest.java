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
import ru.inno.course.player.model.status.Status;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        int newId = service.createPlayer(p.getNick()).getPayload();
        int size = service.getPlayers().getPayload().size();
        Player player = service.getPlayerById(newId).getPayload();

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
        assertEquals(Status.USER_ERROR,service.createPlayer(name).getStatus());
        assertEquals(0, service.getPlayers().getPayload().size());
    }

    @Test
    @DisplayName("Игрок с таким именем уже существует. Игрок не создается.")
    public void shouldNotCreatePlayerNickAlreadyInUse() {
        int newId = service.createPlayer(PLAYER_NAME).getPayload();
        assertEquals(Status.USER_ERROR,service.createPlayer(PLAYER_NAME).getStatus());
        assertEquals(1, service.getPlayers().getPayload().size());
        assertEquals(1, service.getPlayerById(newId).getPayload().getId());
    }

    @Test
    @DisplayName("Можно создать удаленного игрока")
    public void shouldAllowRecreatePlayer() {
        int oldId = service.createPlayer(PLAYER_NAME).getPayload();
        service.deletePlayer(oldId);
        int newId = service.createPlayer(PLAYER_NAME).getPayload();

        assertEquals(1, oldId);
        assertEquals(2, newId);
        assertEquals(1, service.getPlayers().getPayload().size());
    }

    @Test
    @DisplayName("Добавление очков игроку")
    public void shouldAddPointsToPlayer() {
        int newId = service.createPlayer(PLAYER_NAME).getPayload();
        service.addPoints(newId, 10);
        assertEquals(10, service.getPlayerById(newId).getPayload().getPoints());
        service.addPoints(newId, 10);
        assertEquals(20, service.getPlayerById(newId).getPayload().getPoints());
    }

    @DisplayName("Добавление очков игроку. Негативные тесты")
    @ParameterizedTest(name = "{index} => Добавление очков игроку {0}")
    @ValueSource(ints = {0, -10, -100, -1, Integer.MIN_VALUE})
    public void testPointsAddingParams(int points) {
        int newId = service.createPlayer(PLAYER_NAME).getPayload();
        assertEquals(Status.USER_ERROR, service.addPoints(newId, points).getStatus());
        assertEquals(0, service.getPlayerById(newId).getPayload().getPoints());
    }

    @Test
    @DisplayName("Удаление существующего пользователя")
    public void shouldDeletePlayer() {
        int newId = service.createPlayer(PLAYER_NAME).getPayload();
        Player p = service.deletePlayer(newId).getPayload();

        assertEquals(newId, p.getId());
        assertEquals(PLAYER_NAME, p.getNick());
        assertEquals(0, service.getPlayers().getPayload().size());
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя")
    public void shouldNotDeleteUnknownPlayer() {
        int newId = service.createPlayer(PLAYER_NAME).getPayload();
        assertEquals(Status.USER_ERROR, service.deletePlayer(newId + 1).getStatus());
    }
}
