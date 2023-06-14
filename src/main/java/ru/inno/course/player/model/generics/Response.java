package ru.inno.course.player.model.generics;

import ru.inno.course.player.model.status.Status;

import java.util.Objects;

public class Response<T> {

    private Status status;
    private String message;
    private T payload;

    public Response(Status status, String message, T payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response<?> response)) return false;
        return getStatus() == response.getStatus() && Objects.equals(getMessage(), response.getMessage()) && Objects.equals(getPayload(), response.getPayload());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getPayload());
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}
