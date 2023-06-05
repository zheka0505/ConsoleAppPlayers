package ru.inno.course.player;

import org.junit.jupiter.api.*;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JUnitExampleTest {
    private static Path filePath;
    private PlayerService service;

    @BeforeAll
    public static void setUp(){
        filePath = Path.of("data.json"); // получение имени файла
    }

    @BeforeEach
    public void createService(){
        service = new PlayerServiceImpl();
    }

    @AfterEach
    public void deleteFileIfNeeded() throws IOException {
        Files.deleteIfExists(filePath);
    }

    @DisplayName("Тест на добавление очков")
    @Test
    public void shouldAddPointsCorrectly() {
        int id = service.createPlayer("tester");
        service.addPoints(id, 10);
        Player player = service.getPlayerById(id);
        assertEquals(10, player.getPoints());
    }

    @DisplayName("Тест на удаление игрока")
    @Tag("delete") @Tag("service")
    @Test
    public void shouldDeletePlayer() throws IOException {
        int id = service.createPlayer("tester");
        boolean exists = Files.exists(filePath);
        assertTrue(exists);
        service.deletePlayer(id);
        assertEquals(0, service.getPlayers().size());
    }

    @Test
    @Tag("create")
    public void shouldCreateFile() {
        service.createPlayer("tester");
        boolean exists = Files.exists(filePath);
        assertTrue(exists);
        assertEquals(1, service.getPlayers().size());
    }
    
    @Test
    @Tag("create") @Tag("service")
    public void shouldCreate3Players(){
        service.createPlayer("P1");
        service.createPlayer("P2");
        service.createPlayer("P3");
        assertEquals(3, service.getPlayers().size());
    }
}
