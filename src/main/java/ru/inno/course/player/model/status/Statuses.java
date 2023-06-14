package ru.inno.course.player.model.status;


// легкий вариант ограничить возможные значения
// минус: я не обязан его соблюдать
public class Statuses {
    public static String OK = "OK";
    public static String BAD_REQUEST = "BAD_REQUEST";
    public static String INTERNAL_ERROR = "INTERNAL_ERROR";

    public static StatusCls OK_STATUS = new StatusCls("OK");
    public static StatusCls BAD_REQUEST_STATUS = new StatusCls("User error");
    public static StatusCls INTERNAL_ERROR_STATUS = new StatusCls("500");

}
