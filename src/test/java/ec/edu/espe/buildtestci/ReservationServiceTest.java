package ec.edu.espe.buildtestci;

import ec.edu.espe.buildtestci.dto.ReservationResponse;
import ec.edu.espe.buildtestci.model.RoomReservation;
import ec.edu.espe.buildtestci.repository.ReservationRepository;
import ec.edu.espe.buildtestci.service.ReservationService;
import ec.edu.espe.buildtestci.service.UserPolicyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private UserPolicyClient userPolicyClient;
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        userPolicyClient = Mockito.mock(UserPolicyClient.class);
        reservationService = new ReservationService(reservationRepository, userPolicyClient);
    }

    @Test
    void createReservation_validData_shouldSaveAndReturnResponse() {
        // Arrange
        String roomCode = "H206";
        String email = "josue@espe.edu.ec";
        int hours = 2;

        when(reservationRepository.existsByRoomCode(roomCode)).thenReturn(false);
        when(userPolicyClient.isBlocked(email)).thenReturn(false);
        when(reservationRepository.save(any(RoomReservation.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ReservationResponse response = reservationService.createReservation(roomCode, email, hours);

        // Assert
        assertNotNull(response.getReservationId());
        assertEquals(roomCode, response.getRoomCode());
        assertEquals(hours, response.getHours());

        verify(userPolicyClient).isBlocked(email);
        verify(reservationRepository).save(any(RoomReservation.class));
        verify(reservationRepository).existsByRoomCode(roomCode);
    }

    @Test
    void createReservation_invalidEmail_shouldThrow_andNotCallDependencies() {
        // Arrange
        String invalidEmail = "josue@espe.edu.ec";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation("H206", invalidEmail, 2)
        );

        verifyNoInteractions(reservationRepository, userPolicyClient);
    }

    @Test
    void createReservation_hoursOutOfRange_shouldThrow_andNotCallDependencies() {
        // Arrange
        int invalidHours = 9;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation("H206", "josue@espe.edu.ec", invalidHours)
        );

        verifyNoInteractions(reservationRepository, userPolicyClient);
    }

    @Test
    void createReservation_roomAlreadyReserved_shouldThrow() {
        // Arrange
        String roomCode = "H206";
        String email = "josue@espe.edu.ec";
        int hours = 2;

        when(reservationRepository.existsByRoomCode(roomCode)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                reservationService.createReservation(roomCode, email, hours)
        );

        assertEquals("Room ya esta reservado", exception.getMessage());
        verify(reservationRepository).existsByRoomCode(roomCode);
        verify(reservationRepository, never()).save(any());
        verify(userPolicyClient, never()).isBlocked(any());
    }
}
