package ru.inno.course.player;

import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestClass {

//- Создание пользователя с валидными данными
//- Удаление существующего пользователя
//- Добавление валидного кол-ва очков пользователю (10)
//- поверить валидность json на выходе
//- проверить, что корректно вычитывает существующий json

    public static void mai1n(String[] args) throws IOException {
        deleteFileIfNeeded();
        int errorCounter = 0;

        try {
            // Test. Создание пользователя с валидными данными
            PlayerService service = new PlayerServiceImpl();
            int id = createPlayer(service);
            try {
                checkPlayerListSize(service);
                checkFileExists();
            } catch (Exception ex) {
                errorCounter++;
                System.err.println(ex);
            }

            // Test. Удаление существующего пользователя
            int before = service.getPlayers().size();
            service.deletePlayer(id);
            try {
                checkListIsEmpty(service);
            } catch (Exception ex) {
                errorCounter++;
                System.err.println(ex);
            }

            // Test. Добавление валидного кол-ва очков пользователю (10)
            try {
                addPointsToPlayer(service, id);
                checkPlayerHasPoints(service, id);
                checkPlayerListSize(service);
            } catch (Exception ex) {
                errorCounter++;
                System.err.println(ex);
            }

        } catch (Exception ex){
            System.out.println(errorCounter);
        }
    }

    private static void checkListIsEmpty(PlayerService service) {
        if (service.getPlayers().size() == 0) {
            System.out.println("Игрок удалился");
        } else {
            System.err.println("Игрок не удалился");
        }
    }

    private static void checkPlayerHasPoints(PlayerService service, int id) {
        // ПРОВЕРИТЬ, что у игрока 10 очков
        Player player = service.getPlayerById(id);
        if (player.getPoints() == 10) {
            System.out.println("очки назначены корректно");
        } else {
            System.err.println("очки посчитались неправильно." + player.getPoints());
        }
    }

    private static void addPointsToPlayer(PlayerService service, int id) {
        // 1. Создать игрока - DONE
        // 2. Сохранить его id - DONE
        // 3. Назначить игроку с id очков = 10
        service.addPoints(id, 10);
    }

    private static void checkFileExists() {
        // 4. ПРОВЕРИТЬ, что файл data.json существует
        boolean fileExists = Files.exists(Path.of("data.json"));
        if (fileExists) {
            System.out.println("файлик создался");
        } else {
            System.err.println("файлик не создался");
        }
    }

    private static void checkPlayerListSize(PlayerService service) {
        // 3. ПРОВЕРИТЬ, что в списке 1 Игрок
        int currentSize = service.getPlayers().size();
        if (currentSize == 1) {
            System.out.println("игрок сохранен");
        } else {
            System.err.println("что-то пошло не так");
            for (Player player : service.getPlayers()) {
                System.out.println(player);
            }
        }
    }

    private static int createPlayer(PlayerService service) {
        // 1. Вызвать сервис на создание
        // 2. Передать валидные данные
        int id = service.createPlayer("Tester");
        return id;
    }

    private static void deleteFileIfNeeded() throws IOException {
        // Precondition
        Files.deleteIfExists(Path.of("data.json"));
    }

}
