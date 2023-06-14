package ru.inno.course.player.model.generics;

import ru.inno.course.player.model.status.Status;

import java.util.Objects;

public class ResponseInt {

    private Status status; // OK | BAD_REQUEST | INTERNAL_ERROR
    private String message;
    private int value;

    public ResponseInt(Status status, String message, int value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseInt that)) return false;
        return getValue() == that.getValue() && getStatus() == that.getStatus() && Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getValue());
    }

    @Override
    public String toString() {
        return "ResponseInt{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", value=" + value +
                '}';
    }
}
