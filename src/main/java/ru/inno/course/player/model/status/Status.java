package ru.inno.course.player.model.status;

public enum Status {
    OK("OK", 200),
    USER_ERROR("BAD request", 400),
    INTERNAL_ERROR("500",500);


    private String message;
    private int code;

    Status(String message, int code) {

        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
