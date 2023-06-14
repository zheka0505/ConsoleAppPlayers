package ru.inno.course.player;

import ru.inno.course.player.model.Player;
import ru.inno.course.player.model.generics.Response;
import ru.inno.course.player.model.status.Status;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.util.Collection;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PlayerService service = new PlayerServiceImpl();

        printHelp();

        while (true) {
            try {
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("list")) {
                    Response<Collection<Player>> players = service.getPlayers();

                    if (players.getPayload().size() == 0) {
                        System.out.println(players.getMessage());
                    }

                    for (Player player : players.getPayload()) {
                        System.out.println(player);
                    }
                    continue;
                }

                // add Max
                if (command.toLowerCase().startsWith("add ")) {
                    String nick = command.substring(4);
                    Response<Integer> response = service.createPlayer(nick);
                    if (response.getStatus().equals(Status.OK)) {
                        System.out.println(response.getPayload());
                    } else {
                        System.err.println(response.getMessage());
                    }
                    continue;
                }

                // get 1
                if (command.toLowerCase().startsWith("get ")) {
                    String idAsString = command.substring(4);
                    int id = Integer.parseInt(idAsString);
                    Response<Player> response = service.getPlayerById(id);
                    if (response.getStatus().equals(Status.OK)) {
                        System.out.println(response.getPayload());
                        ;
                    } else {
                        System.err.println(response.getMessage());
                    }
                    continue;
                }

                // delete 1
                if (command.toLowerCase().startsWith("delete ")) {
                    String idAsString = command.substring(7);
                    int id = Integer.parseInt(idAsString);
                    Response<Player> response = service.deletePlayer(id);
                    if (response.getStatus().equals(Status.OK)) {
                        System.out.println(response.getPayload());
                        ;
                    } else {
                        System.err.println(response.getMessage());
                    }
                    continue;
                }

                // points 1 20 => [ "1", "20" ]
                if (command.toLowerCase().startsWith("points ")) {
                    String argsString = command.substring(7);
                    String[] arguments = argsString.split(" ");
                    int id = Integer.parseInt(arguments[0]);
                    int points = Integer.parseInt(arguments[1]);
                    Response<Integer> response = service.addPoints(id, points);
                    if (response.getStatus().equals(Status.OK)) {
                        System.out.println(response.getPayload());
                        ;
                    } else {
                        System.err.println(response.getMessage());
                    }
                    continue;
                }

                if (command.equalsIgnoreCase("quit")) {
                    System.exit(0);
                }

                if (command.equalsIgnoreCase("help")) {
                    printHelp();
                    continue;
                }

                System.out.println("Unknown command!");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

    }

    private static void printHelp() {
        System.out.println("help – print this message");
        System.out.println("add <nickname> – add new Player with NICKNAME");
        System.out.println("get <id> – get Player by id");
        System.out.println("list – get all users");
        System.out.println("delete <id> – delete Player by id");
        System.out.println("points <playerId> <pointsToAdd> – add points to Player");
        System.out.println("quit – exit");
    }
}
