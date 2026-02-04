package ec.edu.espe.buildtestci.model;

import java.util.UUID;

public class RoomReservation {

    public enum Status {
        CREATED,
        CONFIRMED
    }

    private final String id;
    private final String roomCode;
    private final String reservedByEmail;
    private final int hours;
    private Status status;

    public RoomReservation(String roomCode, String reservedByEmail, int hours) {
        this.id = UUID.randomUUID().toString();
        this.roomCode = roomCode;
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
        this.status = Status.CREATED;
    }

    public String getId() {
        return id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getReservedByEmail() {
        return reservedByEmail;
    }

    public int getHours() {
        return hours;
    }

    public Status getStatus() {
        return status;
    }

    public void confirm() {
        this.status = Status.CONFIRMED;
    }
}
