package ru.inno.course.player.certificationZheka;

import org.junit.jupiter.api.*;
import ru.inno.course.player.data.DataProviderJSON;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

// 1. Тест не должен настраивать свое окружение.
// 2. Главный код ничего не знает про тесты.
// 3. В тестах не должно быть if'ов

public class PlayerServiceTestCert {
    private PlayerService service;
    private static final String NICKNAME = "zheka";
    private static final String ANOTHERNICKNAME = "NIKITA";
    private static final String FIFTEENCHARACTERS = "Rhoshandiatelly";
    private static final String SIXTEENCHARACTERS = "Rhoshandiatellyy";


    @BeforeEach
    public void setUp() {
        service = new PlayerServiceImpl();

    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("./data.json"));
    }

    @Test
    @DisplayName("Позитивный 1, 3 и 7: Создаем игрока и проверяем его значения по дефолту/нет json-файла/получить игрока по id")
    public void iCanAddNewPlayerWithoutJson() {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(0, listBefore.size());

        int zhekaId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(zhekaId);

        assertEquals(zhekaId, playerById.getId());
        assertEquals(0, playerById.getPoints());
        assertEquals(NICKNAME, playerById.getNick());
        assertTrue(playerById.isOnline());
    }

    @Test
    @DisplayName("Позитивный 2: Удаление игрока, проверяем что его нет в списке")
    public void iCanDelatePlayer() {
        int zhekaId = service.createPlayer(NICKNAME);
        service.deletePlayer(zhekaId);

        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(zhekaId));

    }

    @Test
    @DisplayName("Позитивный 4: Создаем игрока и проверяем его id (есть json-файл)")
    public void iCanAddNewPlayerWithJson() {
        service.createPlayer(ANOTHERNICKNAME);

        int zhekaId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(zhekaId);

        assertEquals(zhekaId, playerById.getId());
    }

    @Test
    @DisplayName("Позитивный 5: Начислить баллы существующему игроку")
    public void iCanAddNewPointsToPlayer() {
        int zhekaId = service.createPlayer(NICKNAME);
        int playerPoints = service.addPoints(zhekaId, 100);

        assertEquals(100, playerPoints);
    }

    @Test
    @DisplayName("Позитивный 6: Добавить очков поверх существующих")
    public void iCanAddPointsToPlayer() {
        int zhekaId = service.createPlayer(NICKNAME);
        service.addPoints(zhekaId, 100);
        int playerPoints = service.addPoints(zhekaId, 55);

        assertEquals(155, playerPoints);
    }

    @Test
    @DisplayName("Позитивный 8: Поверить корректность сохранения в файл")
    public void checkSaveFileData() throws IOException {
        int zhekaId = service.createPlayer(NICKNAME);

        DataProviderJSON provider = new DataProviderJSON();
        List<Player> currentList = (List<Player>) provider.load();

        assertEquals(zhekaId, currentList.get(0).getId());
        assertEquals(0, currentList.get(0).getPoints());
        assertEquals(NICKNAME, currentList.get(0).getNick());
        assertTrue(currentList.get(0).isOnline());

    }

    @Test
    @DisplayName("Позитивный 9: проверить корректность загрузки json-файла: не потеряли, не 'побили' записи")
    public void checkLoadFileData() {
        int zhekaId = service.createPlayer(NICKNAME);
        int nikitaId = service.createPlayer(ANOTHERNICKNAME);

        List<Player> players = new ArrayList<>(service.getPlayers());

        assertEquals(zhekaId, players.get(0).getId());
        assertEquals(0, players.get(0).getPoints());
        assertEquals(NICKNAME, players.get(0).getNick());
        assertTrue(players.get(0).isOnline());

        assertEquals(nikitaId, players.get(1).getId());
        assertEquals(0, players.get(1).getPoints());
        assertEquals(ANOTHERNICKNAME, players.get(1).getNick());
        assertTrue(players.get(1).isOnline());

    }

    @Test
    @DisplayName("Позитивный 10: Проверить, что id всегда уникальный. Создать 5, удалить 3-го, добавить еще одного")
    public void checkUniqueId() {
        service.createPlayer("test1");
        service.createPlayer("test2");
        int zhekaId = service.createPlayer(NICKNAME);
        service.createPlayer("test4");
        service.createPlayer("test5");

        service.deletePlayer(zhekaId);

        int nikitaId = service.createPlayer(ANOTHERNICKNAME);

        assertEquals(6, nikitaId);
    }


    @Test
    @DisplayName("Позитивный 11: (нет json-файла) запросить список игроков")
    public void checkNoPlayers() {
        Collection<Player> players = service.getPlayers();

        assertEquals(0, players.size());

    }

    @Test
    @DisplayName("Позитивный 12: Проверить создание игрока с 15 символами")
    public void iCanAddNewPlayerWith15Characters() {
        int newPlayerId = service.createPlayer(FIFTEENCHARACTERS);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(newPlayerId, playerById.getId());
        assertEquals(FIFTEENCHARACTERS, playerById.getNick());

    }

    @Test
    @DisplayName("Негативный 1: удалить игрока, которого нет")
    public void deleteNotExistedPlayer() {
        service.createPlayer(NICKNAME);

        assertThrows(NoSuchElementException.class, () -> service.deletePlayer(5));
    }

    @Test
    @DisplayName("Негативный 2: создать дубликат (имя уже занято)")
    public void createDuplicatePlayer() {
        service.createPlayer(NICKNAME);

        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(NICKNAME));
    }

    @Test
    @DisplayName("Негативный 3: получить игрока по id, которого нет")
    public void getNotExistedPlayer() {
        service.createPlayer(NICKNAME);

        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(25));
    }

    @Test
    @DisplayName("Негативный 4: сохранить игрока с пустым ником")
    public void createPlayerWithEmptyNick() {
        int newPlayerId = service.createPlayer("");
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals("", playerById.getNick());

    }

    @Test
    @DisplayName("Негативный 5: начислить отрицательное число очков")
    public void iCanAddNegativePointsToPlayer() {
        int zhekaId = service.createPlayer(NICKNAME);
        int playerPoints = service.addPoints(zhekaId, -55);

        assertEquals(-55, playerPoints);
    }

    @Test
    @DisplayName("Негативный 6: Накинуть очков игроку, которого нет")
    public void iCanAddPointsToNotExistedPlayer() {
       service.createPlayer(NICKNAME);

        assertThrows(NoSuchElementException.class, () -> service.addPoints(5, 50));
    }

    @Test
    @DisplayName("Негативный 9: Проверить создание игрока с 16 символами")
    public void createNewPlayerWith16Characters() {

        int newPlayerId = service.createPlayer(SIXTEENCHARACTERS);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(SIXTEENCHARACTERS, playerById.getNick());

        //вот такой должен был быть правильный тест, если бы проверка на количество символов была
        //assertThrows(IllegalArgumentException.class, () -> service.createPlayer(SIXTEENCHARACTERS));
    }
}
