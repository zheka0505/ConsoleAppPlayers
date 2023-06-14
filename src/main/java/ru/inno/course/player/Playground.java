package ru.inno.course.player;

import ru.inno.course.player.model.generics.Pair;
import ru.inno.course.player.model.status.Status;
import ru.inno.course.player.model.status.StatusCls;
import ru.inno.course.player.model.status.Statuses;

public class Playground {

    public static void main(String[] args) {
        // Custom enum
        StatusCls myStatusOk1 = Statuses.OK_STATUS;
        StatusCls myStatusOk2 = Statuses.OK_STATUS;

        // Enum
        Status myError1 = Status.USER_ERROR;
        Status myError2 = Status.USER_ERROR;

        Pair<String, Integer> myPair = new Pair<>("Test", 10);
    }
}
