package ru.inno.course.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileGeneratorTest {
    private PlayerService service;
    private final static String PLAYER_NAME = "Player name";
    private final static String FILE_CONTENT = """
            [{"id":1,"nick":"Player name_1","points":10,"online":true},{"id":2,"nick":"Player name_2","points":20,"online":true},{"id":3,"nick":"Player name_3","points":30,"online":true}]
            """.trim();
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
    @DisplayName("проверить, что корректно сформирован json-файл (после парсинга итогового файла, коллекции равны)")
    public void shouldGenerateFileProperly() throws IOException {
        int p1 = service.createPlayer(PLAYER_NAME + "_1");
        int p2 = service.createPlayer(PLAYER_NAME + "_2");
        int p3 = service.createPlayer(PLAYER_NAME + "_3");

        service.addPoints(p1, 10);
        service.addPoints(p2, 20);
        service.addPoints(p3, 30);

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(1, lines.size());

        String fileContentAsIs = lines.get(0);
        assertEquals(FILE_CONTENT, fileContentAsIs);
    }

    @Test
    @DisplayName("проверить, что корректно прочитан json-файл (вы создали корректный json-файл и система его правильно прочитала)")
    public void shouldLoadFileProperly() throws IOException {
        Files.write(filePath, FILE_CONTENT.getBytes());
        service = new PlayerServiceImpl();
        assertEquals(3, service.getPlayers().size());
        assertEquals(PLAYER_NAME+"_1", service.getPlayerById(1).getNick());
        assertEquals(10, service.getPlayerById(1).getPoints());

        assertEquals(PLAYER_NAME+"_2", service.getPlayerById(2).getNick());
        assertEquals(20, service.getPlayerById(2).getPoints());

        assertEquals(PLAYER_NAME+"_3", service.getPlayerById(3).getNick());
        assertEquals(30, service.getPlayerById(3).getPoints());
    }
}
