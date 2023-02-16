package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryDouble;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service;
    final ReservationRepositoryDouble reservationRepository = new ReservationRepositoryDouble();
    final HostRepositoryDouble hostRepository = new HostRepositoryDouble();
    final GuestRepositoryDouble guestRepository = new GuestRepositoryDouble();

    @BeforeEach
    void setUp() {
        service = new ReservationService(reservationRepository, hostRepository, guestRepository);
    }

    @Test
    void shouldFindByHostId() throws DataException {
        String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
        List<Reservation> actual = service.findByHost(hostId);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldNotFindFakeId() throws DataException {
        String hostId = "lfjasld";
        List<Reservation> actual = service.findByHost(hostId);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldCalculateTwoWeekNights() throws DataException {
        String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
        int reservationId = 1;
        Reservation reservation = reservationRepository.findById(reservationId, hostId);
        BigDecimal actual = service.calculateTotal(hostId, reservation);
        assertEquals(BigDecimal.valueOf(400), actual);
    }
}