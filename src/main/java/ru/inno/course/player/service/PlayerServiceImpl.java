package ru.inno.course.player.service;

import ru.inno.course.player.data.DataProvider;
import ru.inno.course.player.data.DataProviderJSON;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.model.generics.Response;
import ru.inno.course.player.model.status.Status;

import java.util.*;

public class PlayerServiceImpl implements PlayerService {
    private Map<Integer, Player> players;
    private Set<String> nicknames;
    private int counter = 0;
    private final DataProvider provider;

    public PlayerServiceImpl() {
        provider = new DataProviderJSON();
        initStorages();
    }

    @Override
    public Response<Player> getPlayerById(int id) {
        if (!this.players.containsKey(id)) {
            new Response<>(Status.USER_ERROR, "No such user: " + id, null);
        }

        return new Response<>(Status.OK, "Ok", this.players.get(id));
    }

    @Override
    public Response<Collection<Player>> getPlayers() {
        Collection<Player> values = this.players.values();
        String text = "Ok";
        if (values.isEmpty()) {
            text = "empty collection";
        }

        return new Response<>(Status.OK, text, values);
    }

    @Override
    public Response<Integer> createPlayer(String nickname) {
        if (nickname == null) {
            return new Response<>(Status.USER_ERROR, "Nickname is required", -1);
        }

        if (nickname.isBlank()) {
            return new Response<>(Status.USER_ERROR, "Nickname is blank", -1);
        }
        if (nicknames.contains(nickname)) {
            return new Response<>(Status.USER_ERROR, "Nickname is already in use: " + nickname, -1);
        }

        counter++;
        Player player = new Player(counter, nickname, 0, true);
        this.players.put(player.getId(), player);
        this.nicknames.add(nickname);
        saveToFile();
        return new Response<>(Status.OK, "Created", player.getId());
    }

    @Override
    public Response<Player> deletePlayer(int id) {
        if (!this.players.containsKey(id)) {
            return new Response<>(Status.USER_ERROR, "No such user: " + id, null);
        }

        Player p = this.players.remove(id);
        nicknames.remove(p.getNick());
        saveToFile();
        return new Response<>(Status.OK, "Deleted", p);
    }

    @Override
    public Response<Integer> addPoints(int playerId, int points) {
        if (!this.players.containsKey(playerId)) {
            return new Response<>(Status.USER_ERROR, "No such user: " + playerId, -1);
        }

        if (points < 1) {
            return new Response<>(Status.USER_ERROR, "Points must be greater than 0: " + points, -1);
        }

        Player player = this.players.get(playerId);
        int currentPoints = player.getPoints();
        int newPoints = currentPoints + points;
        player.setPoints(newPoints);
        saveToFile();
        return new Response<>(Status.OK, "Added", player.getPoints());
    }

    private void initStorages() {
        Collection<Player> currentList = Collections.EMPTY_LIST;
        try {
            currentList = provider.load();
        } catch (Exception ex) {
            System.err.println("File loading error. " + ex);
        }

        players = new HashMap<>();
        nicknames = new HashSet<>();

        for (Player player : currentList) {
            players.put(player.getId(), player);
            nicknames.add(player.getNick());
            if (player.getId() > counter) {
                counter = player.getId();
            }
        }
    }

    private void saveToFile() {
        try {
            this.provider.save(players.values());
        } catch (Exception ex) {
            System.err.println("File saving error");
        }
    }

}
