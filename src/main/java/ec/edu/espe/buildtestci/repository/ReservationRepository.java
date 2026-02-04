package ec.edu.espe.buildtestci.repository;

import ec.edu.espe.buildtestci.model.RoomReservation;

import java.util.Optional;

public interface ReservationRepository {

    RoomReservation save(RoomReservation reservation);
    Optional<RoomReservation> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);
}
