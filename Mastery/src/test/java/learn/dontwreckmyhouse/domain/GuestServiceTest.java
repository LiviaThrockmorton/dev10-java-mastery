package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestService service;
    final GuestRepositoryDouble guestRepository = new GuestRepositoryDouble();

    @BeforeEach
    void setUp() {
        service = new GuestService(guestRepository);
    }

    @Test
    void shouldFindByEmail() {
        String email = "wkuhlie@patch.com";
        List<Guest> actual = service.findByEmail(email);
        assertEquals("wkuhlie@patch.com", actual.get(0).getGuestEmail());
        assertEquals("(704) 3740857", actual.get(0).getGuestPhone());
    }

    @Test
    void shouldNotFindByNullEmail() {
        List<Guest> actual = service.findByEmail(null);
        assertNull(actual.get(0));
    }
}