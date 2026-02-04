package ec.edu.espe.buildtestci.dto;

public class ReservationResponse {

    private final String reservationId;
    private final String roomCode;
    private final int hours;

    public ReservationResponse(String reservationId, String roomCode, int hours) {
        this.reservationId = reservationId;
        this.roomCode = roomCode;
        this.hours = hours;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public int getHours() {
        return hours;
    }
}
