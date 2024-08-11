package ru.inno.course.player.certificationZheka;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.inno.course.player.ext.BeforeEachDemo;
import ru.inno.course.player.ext.MyTestWatcher;
import ru.inno.course.player.ext.PlayersAndPointsProvider;
import ru.inno.course.player.ext.PointsProvider;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

// 1. Тест не должен настраивать свое окружение.
// 2. Главный код ничего не знает про тесты.
// 3. В тестах не должно быть if'ов

public class PlayerServiceTestCert {
    private PlayerService service;
    private static final String NICKNAME = "zheka";


    @BeforeEach
    public void setUp() {
        service = new PlayerServiceImpl();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("./data.json"));
    }

    @Test
    @DisplayName("Позитивный 1: Создаем игрока и проверяем его значения по дефолту")
    public void iCanAddNewPlayer() {
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
        Player playerById = service.deletePlayer(zhekaId);

        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(zhekaId));

    }




}
