package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryDouble;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service;
    final ReservationRepositoryDouble reservationRepository = new ReservationRepositoryDouble();
    final HostRepositoryDouble hostRepository = new HostRepositoryDouble();

    @BeforeEach
    void setUp() {
        service = new ReservationService(reservationRepository, hostRepository);
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
}