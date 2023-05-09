package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryDouble;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
        assertEquals(3, actual.size());
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
        BigDecimal actual = service.calculateTotal(reservation);
        assertEquals(BigDecimal.valueOf(400.00).setScale(2, RoundingMode.HALF_UP), actual);
    }

    @Test
    void shouldCalculateWeekNightAndWeekendNight() throws DataException {
        String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
        int reservationId = 2;
        Reservation reservation = reservationRepository.findById(reservationId, hostId);
        BigDecimal actual = service.calculateTotal(reservation);
        assertEquals(BigDecimal.valueOf(450.00).setScale(2, RoundingMode.HALF_UP), actual);
    }

    @Test
    void shouldCalculateWeekendToWeekDay() throws DataException {
        String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
        int reservationId = 3;
        Reservation reservation = reservationRepository.findById(reservationId, hostId);
        BigDecimal actual = service.calculateTotal(reservation);
        assertEquals(BigDecimal.valueOf(450.00).setScale(2, RoundingMode.HALF_UP), actual);
    }

    @Test
    void shouldMake() throws DataException {
        Reservation r = new Reservation();
        r.setStart(LocalDate.of(2025, 1, 1));
        r.setEnd(LocalDate.of(2025, 2, 1));
        r.setGuestId(1);
        r.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Result<Reservation> result = service.make(r);
        assertTrue(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(4, actual.size());
    }

    @Test
    void shouldNotMakeNullReservation() throws DataException {
        Reservation r = new Reservation();
        Result<Reservation> result = service.make(r);
        assertFalse(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotMakeNoGuestId() throws DataException {
        Reservation r = new Reservation();
        r.setStart(LocalDate.of(2025, 1, 1));
        r.setEnd(LocalDate.of(2025, 2, 1));
        r.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Result<Reservation> result = service.make(r);
        assertFalse(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotMakeNoHostId() throws DataException {
        Reservation r = new Reservation();
        r.setStart(LocalDate.of(2025, 1, 1));
        r.setEnd(LocalDate.of(2025, 2, 1));
        r.setGuestId(1);
        Result<Reservation> result = service.make(r);
        assertFalse(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotMakeEndBeforeStart() throws DataException {
        Reservation r = new Reservation();
        r.setStart(LocalDate.of(2025, 2, 1));
        r.setEnd(LocalDate.of(2025, 1, 1));
        r.setGuestId(1);
        r.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Result<Reservation> result = service.make(r);
        assertFalse(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(3, actual.size());
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setStart(LocalDate.of(2023, 10, 10));
        r.setEnd(LocalDate.of(2023, 10, 13));
        r.setGuestId(663);
        r.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setTotal(BigDecimal.valueOf(400));
        Result<Reservation> result = service.update(r);
        assertTrue(result.isSuccess());
        assertEquals(LocalDate.of(2023, 10, 13), r.getEnd());
    }

    @Test
    void shouldNotUpdateWrongGuestId() throws DataException {
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setStart(LocalDate.of(2023, 10, 10));
        r.setEnd(LocalDate.of(2023, 10, 13));
        r.setGuestId(7);
        r.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setTotal(BigDecimal.valueOf(400));
        Result<Reservation> result = service.update(r);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNullReservation() throws DataException {
        Reservation r = null;
        Result<Reservation> result = service.update(r);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldCancel() throws DataException {
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setStart(LocalDate.of(2023, 10, 10));
        r.setEnd(LocalDate.of(2023, 10, 13));
        r.setGuestId(663);
        Result<Reservation> result = service.cancel(r);
        assertTrue(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(2, actual.size());
    }

    @Test
    void shouldNotCancelWrongGuestId() throws DataException {
        Reservation r = new Reservation();
        r.setReservationId(1);
        r.setStart(LocalDate.of(2023, 10, 10));
        r.setEnd(LocalDate.of(2023, 10, 13));
        r.setGuestId(7);
        Result<Reservation> result = service.cancel(r);
        assertFalse(result.isSuccess());
        List<Reservation> actual = service.findByHost("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(3, actual.size());
    }
}