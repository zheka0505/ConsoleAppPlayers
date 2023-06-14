package ru.inno.course.player.service;

import ru.inno.course.player.model.Player;
import ru.inno.course.player.model.generics.Response;

import java.util.Collection;

public interface PlayerService {
    /**
     * Возвращает информацию об игроке по его id
     *
     * @param id id игрока
     * @return Response<Player>. Статус, сообщение и игрок
     */
    Response<Player> getPlayerById(int id);

    /**
     * Возвращает список игроков
     *
     * @return Collection<Player>
     */
    Response<Collection<Player>> getPlayers();

    // создать игрока (возвращает id нового игрока)
    Response<Integer> createPlayer(String nickname);

    // удалить игрока по id'шнику, вернет удаленного игрока
    Response<Player> deletePlayer(int id);

    // Добавить очков игроку. Возвращает обновленный счет
    Response<Integer> addPoints(int playerId, int points);
}
