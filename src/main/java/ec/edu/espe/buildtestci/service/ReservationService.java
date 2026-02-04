package ec.edu.espe.buildtestci.service;

import ec.edu.espe.buildtestci.dto.ReservationResponse;
import ec.edu.espe.buildtestci.model.RoomReservation;
import ec.edu.espe.buildtestci.repository.ReservationRepository;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserPolicyClient userPolicyClient;

    public ReservationService(ReservationRepository reservationRepository, UserPolicyClient userPolicyClient) {
        this.reservationRepository = reservationRepository;
        this.userPolicyClient = userPolicyClient;
    }

    public ReservationResponse createReservation(String roomCode, String email, int hours) {
        if (roomCode == null || roomCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Room code cannot be null or empty");
        }

        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        if (hours <= 0 || hours > 8) {
            throw new IllegalArgumentException("Hours must be greater than 0 and less than or equal to 8");
        }

        if (reservationRepository.existsByRoomCode(roomCode)) {
            throw new IllegalStateException("Room is already reserved");
        }

        if (userPolicyClient.isBlocked(email)) {
            throw new IllegalStateException("User is blocked by institutional policies");
        }

        RoomReservation reservation = new RoomReservation(roomCode, email, hours);
        RoomReservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getRoomCode(), savedReservation.getHours());
    }
}
