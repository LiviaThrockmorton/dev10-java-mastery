package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/seed-2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/test-2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_DIR_PATH = "./data/reservations_test";

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setupTest() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByHost() {
        String hostId = "test-2e72f86c-b8fe-4265-b4f1-304dea8762db";
        List<Reservation> actual = repository.findByHost(hostId);
        assertNotNull(actual);
        assertEquals(12, actual.size());
        assertEquals(BigDecimal.valueOf(400), actual.get(0).getTotal());
    }

    @Test
    void shouldNotFindNullHostId() {
        List<Reservation> actual = repository.findByHost(null);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation r = new Reservation();

        r.setHostId("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setStart(LocalDate.of(2023, 2, 26));
        r.setEnd(LocalDate.of(2023, 2, 27));
        r.setGuestId(1);
        r.setTotal(BigDecimal.valueOf(400));

        Reservation actual = repository.add(r);
        assertNotNull(actual);
        assertEquals(13, r.getReservationId());
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation r = new Reservation();
        r.setHostId("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setReservationId(6);
        r.setStart(LocalDate.of(2024, 3, 1));
        r.setEnd(LocalDate.of(2023, 3, 2));
        r.setGuestId(151);
        r.setTotal(BigDecimal.valueOf(1500));
        boolean success = repository.update(r);
        assertTrue(success);

        List<Reservation> actual = repository.findByHost("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(12, actual.size());
        assertEquals(LocalDate.of(2023, 3, 2), actual.get(5).getEnd());
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Reservation r = new Reservation();
        r.setHostId("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setReservationId(20);
        r.setStart(LocalDate.of(2024, 3, 1));
        r.setEnd(LocalDate.of(2023, 3, 2));
        r.setGuestId(151);
        r.setTotal(BigDecimal.valueOf(1500));
        boolean success = repository.update(r);
        assertFalse(success);
    }

    @Test
    void shouldCancel() throws DataException {
        Reservation r = new Reservation();
        r.setHostId("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setReservationId(12);
        r.setGuestId(735);
        boolean success = repository.cancel(r);
        assertTrue(success);
        List<Reservation> actual = repository.findByHost("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(11, actual.size());
    }

    @Test
    void shouldNotCancelMissing() throws DataException {
        Reservation r = new Reservation();
        r.setHostId("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        r.setReservationId(12);
        r.setGuestId(20);
        boolean success = repository.cancel(r);
        assertFalse(success);
        List<Reservation> actual = repository.findByHost("test-2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(12, actual.size());
    }
}