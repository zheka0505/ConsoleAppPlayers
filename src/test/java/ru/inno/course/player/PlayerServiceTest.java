package ru.inno.course.player;

import org.junit.jupiter.api.Test;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


// желто-оранжевый - тест упал на проверке
// зеленый - тест прошел
// красный - тест взорвался, не удалось

public class PlayerServiceTest {

    @Test
    public void iCanAddNewPlayer() throws Exception {
        String nickname = "Nikita";
        Files.deleteIfExists(Path.of("./data.json"));
        PlayerService service = new PlayerServiceImpl();

        Collection<Player> listBefore = service.getPlayers();
        assertEquals(0, listBefore.size());

        int nikitaId = service.createPlayer(nickname);
        Player playerById = service.getPlayerById(nikitaId);

        assertEquals(nikitaId, playerById.getId());
        assertEquals(0, playerById.getPoints());
        assertEquals(nickname, playerById.getNick());
        assertTrue(playerById.isOnline());
    }

    @Test
    public void iCannotCreateADuplicate() throws IOException {
        String nickname = "Nikita";
        Files.deleteIfExists(Path.of("./data.json"));
        PlayerService service = new PlayerServiceImpl();
        service.createPlayer(nickname);
        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(nickname));
    }

    @Test
    public void iCannotGetEmptyUser() throws IOException {
        Files.deleteIfExists(Path.of("./data.json"));
        PlayerService service = new PlayerServiceImpl();
        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(9999));
    }


}

