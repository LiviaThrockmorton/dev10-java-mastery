package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/guests-seed.csv";
    static final String TEST_FILE_PATH = "./data/guests-test.csv";

    GuestFileRepository repository = new GuestFileRepository(SEED_FILE_PATH);

    @BeforeEach
    void setupTest() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Guest> guests = repository.findAll();
        assertEquals(1, guests.size());
    }

    @Test
    void shouldFindById() {
        Guest guest = repository.findById(1);
        assertNotNull(guest);
        assertEquals("Sullivan", guest.getGuestFirstName());
    }

    @Test
    void shouldNotFindFakeID() {
        Guest guest = repository.findById(2);
        assertNull(guest);
    }
}