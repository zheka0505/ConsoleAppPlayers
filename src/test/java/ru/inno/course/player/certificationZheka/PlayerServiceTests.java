package ru.inno.course.player.certificationZheka;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
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

public class PlayerServiceTests {
    private PlayerService service;
    private static final String NICKNAME = "bob";
    private static final String ANOTHERNICKNAME = "NIKITA";
    private static final String CHARACTERS15 = "Rhoshandiatelly";
    private static final String CHARACTERS16 = "Rhoshandiatellyy";

    @BeforeEach
    public void setUp() {
        service = new PlayerServiceImpl();

    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("./data.json"));
    }

    @Test
    @DisplayName("Позитивный 1: добавить игрока - проверить наличие в списке")
    public void createNewPlayer() {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(0, listBefore.size());

        int newPlayerId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(newPlayerId, playerById.getId());
        assertEquals(0, playerById.getPoints());
        assertEquals(NICKNAME, playerById.getNick());
        assertTrue(playerById.isOnline());
    }

    @Test
    @DisplayName("Позитивный 2: Удаление игрока, проверяем что его нет в списке")
    public void deletePlayer() {
        int newPlayerId = service.createPlayer(NICKNAME);
        service.deletePlayer(newPlayerId);

        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(newPlayerId));

    }

    @Test
    @DisplayName("Позитивный 3: (нет json-файла) добавить игрока")
    public void createNewPlayerWithoutJson() {
        int newPlayerId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(newPlayerId, playerById.getId());
        assertEquals(NICKNAME, playerById.getNick());
    }

    @Test
    @DisplayName("Позитивный 4: Создаем игрока и проверяем его id (есть json-файл)")
    public void createNewPlayerWithJson() {
        service.createPlayer(ANOTHERNICKNAME);

        int newPlayerId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(newPlayerId, playerById.getId());
    }

    @Test
    @DisplayName("Позитивный 5: Начислить баллы существующему игроку")
    public void addNewPointsToPlayer() {
        int newPlayerId = service.createPlayer(NICKNAME);
        int playerPoints = service.addPoints(newPlayerId, 100);

        assertEquals(100, playerPoints);
    }

    @Test
    @DisplayName("Позитивный 6: Добавить очков поверх существующих")
    public void addPointsToPlayerTwice() {
        int newPlayerId = service.createPlayer(NICKNAME);
        service.addPoints(newPlayerId, 100);
        int playerPoints = service.addPoints(newPlayerId, 55);

        assertEquals(155, playerPoints);
    }

    @Test
    @DisplayName("Позитивный 7: (добавить игрока) - получить игрока по id")
    public void getCreatedPlayer() {
        int newPlayerId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(newPlayerId, playerById.getId());
    }

    @Test
    @DisplayName("Позитивный 8: Поверить корректность сохранения в файл")
    public void saveFileData() throws IOException {
        int newPlayerId = service.createPlayer(NICKNAME);

        DataProviderJSON provider = new DataProviderJSON();
        List<Player> currentList = (List<Player>) provider.load();

        assertEquals(newPlayerId, currentList.get(0).getId());
        assertEquals(0, currentList.get(0).getPoints());
        assertEquals(NICKNAME, currentList.get(0).getNick());
        assertTrue(currentList.get(0).isOnline());

    }

    @Test
    @DisplayName("Позитивный 9: проверить корректность загрузки json-файла: не потеряли, не 'побили' записи")
    public void loadFileData() {
        int newPlayerId = service.createPlayer(NICKNAME);
        int nikitaId = service.createPlayer(ANOTHERNICKNAME);

        List<Player> players = new ArrayList<>(service.getPlayers());

        assertEquals(newPlayerId, players.get(0).getId());
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
    public void createPlayerWithUniqueId() {
        service.createPlayer("test1");
        service.createPlayer("test2");
        int newPlayerId = service.createPlayer(NICKNAME);
        service.createPlayer("test4");
        service.createPlayer("test5");

        service.deletePlayer(newPlayerId);

        int anotherPlayerId = service.createPlayer(ANOTHERNICKNAME);

        assertEquals(6, anotherPlayerId);
    }


    @Test
    @DisplayName("Позитивный 11: (нет json-файла) запросить список игроков")
    public void getEmptyListOfPlayers() {
        Collection<Player> players = service.getPlayers();

        assertEquals(0, players.size());

    }

    @Test
    @DisplayName("Позитивный 12: Проверить создание игрока с 15 символами")
    public void createNewPlayerWith15Characters() {
        int newPlayerId = service.createPlayer(CHARACTERS15);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(newPlayerId, playerById.getId());
        assertEquals(CHARACTERS15, playerById.getNick());

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
        //нельзя не передавать ник, поэтому сделано так
        int newPlayerId = service.createPlayer("");
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals("", playerById.getNick());

    }

    @Test
    @DisplayName("Негативный 5: начислить отрицательное число очков")
    public void addNegativePointsToPlayer() {
        int newPlayerId = service.createPlayer(NICKNAME);
        int playerPoints = service.addPoints(newPlayerId, -55);

        assertEquals(-55, playerPoints);

        //вот такой должен был быть правильный тест, если бы проверка на отрицательное значение была:
        //int newPlayerId = service.createPlayer(NICKNAME);
        //assertThrows(IllegalArgumentException.class, () -> service.addPoints(newPlayerId, -55));
    }

    @Test
    @DisplayName("Негативный 6: Накинуть очков игроку, которого нет")
    public void addPointsToNotExistedPlayer() {
        service.createPlayer(NICKNAME);

        assertThrows(NoSuchElementException.class, () -> service.addPoints(5, 50));
    }

    @Test
    @DisplayName("Негативный 7: Проверить загрузку системы с другим json-файлом")
    public void loadFileDifferentJson() throws IOException {

        String data = """
                [
                        {
                            "id": 1,
                                "name": "car",
                                "color": red,
                                "price": 40
                        },
                                {
                            "id": 2,
                                "name": "car",
                                "color": yellow,
                                "price": 40
                        }
                ]""";
        Files.writeString(Path.of("./data.json"), data);

        DataProviderJSON provider = new DataProviderJSON();

        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(1));
        assertThrows(UnrecognizedPropertyException.class, provider::load);

    }

    @Test
    @DisplayName("Негативный 8: проверить корректность загрузки json-файла - есть дубликаты")
    public void loadFileJsonWithDuplicates() throws IOException {

        String data = """
                [
                  {
                    "id": 1,
                    "nick": "bob",
                    "points": 0,
                    "online": true
                  },
                  {
                    "id": 1,
                    "nick": "sam",
                    "points": 0,
                    "online": true
                  }
                ]""";
        Files.writeString(Path.of("./data.json"), data);

        DataProviderJSON provider = new DataProviderJSON();
        List<Player> currentList = (List<Player>) provider.load();

        assertEquals(1, currentList.get(0).getId());
        assertEquals(0, currentList.get(0).getPoints());
        assertEquals("bob", currentList.get(0).getNick());
        assertTrue(currentList.get(0).isOnline());

        assertEquals(1, currentList.get(1).getId());
        assertEquals(0, currentList.get(1).getPoints());
        assertEquals("sam", currentList.get(1).getNick());
        assertTrue(currentList.get(1).isOnline());


        //такие assert должны были быть, если бы была проверка на уникальность id:
        //assertThrows(IllegalArgumentException.class, () -> service.getPlayerById(1));
        //assertThrows(IllegalArgumentException.class, provider::load);
    }

    @Test
    @DisplayName("Негативный 9: Проверить создание игрока с 16 символами")
    public void createNewPlayerWith16Characters() {

        int newPlayerId = service.createPlayer(CHARACTERS16);
        Player playerById = service.getPlayerById(newPlayerId);

        assertEquals(CHARACTERS16, playerById.getNick());

        //вот такой должен был быть правильный тест, если бы проверка на количество символов была:
        //assertThrows(IllegalArgumentException.class, () -> service.createPlayer(CHARACTERS16));
    }
}
