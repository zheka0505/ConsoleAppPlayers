package ru.inno.course.player.model.generics;

import ru.inno.course.player.model.Player;
import ru.inno.course.player.model.status.Status;

import java.util.Objects;

public class ResponsePlayer {

    private Status status;
    private String message;
    private Player body;

    public ResponsePlayer(Status status, String message, Player body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Player getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponsePlayer that)) return false;
        return getStatus() == that.getStatus() && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getBody());
    }

    @Override
    public String toString() {
        return "ResponsePlayer{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", body=" + body +
                '}';
    }
}
