package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    void shouldNotFindByNullHostId() {
        List<Reservation> actual = repository.findByHost(null);
        assertEquals(0, actual.size());
    }
}