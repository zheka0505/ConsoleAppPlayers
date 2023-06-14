package ru.inno.course.player.model.generics;

import ru.inno.course.player.model.Player;
import ru.inno.course.player.model.status.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ResponseCollection {

    private Status status;
    private String message;
    private Collection<Player> body;

    public ResponseCollection(Status status, String message, Collection<Player> body) {
        this.status = status;
        this.message = message;
        this.body = body;

        ArrayList<String> list = new ArrayList<>();
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Collection<Player> getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseCollection that)) return false;
        return getStatus() == that.getStatus() && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getBody());
    }

    @Override
    public String toString() {
        return "ResponseCollection{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", body=" + body +
                '}';
    }
}
