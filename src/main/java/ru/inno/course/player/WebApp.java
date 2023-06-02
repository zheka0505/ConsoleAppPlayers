package ru.inno.course.player;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.validation.Validator;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.util.NoSuchElementException;

public class WebApp {

    public static void main(String[] args) {
        PlayerService service = new PlayerServiceImpl();

        Javalin app = Javalin.create();


        app.get("/", context -> {
            context.status(200);
            context.json(service.getPlayers());
        });

        app.get("/{id}", context -> {

            Validator<Integer> param = context.pathParamAsClass("id", Integer.class);
            Player p;
            try {
                p = service.getPlayerById(param.get());
                context.status(HttpStatus.OK);
                context.json(p);
            } catch (NoSuchElementException ex) {
                context.status(HttpStatus.NOT_FOUND);
                context.result("No such player");
            }
        });

        app.start(7070);
    }
}
