package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {
    HostService service;
    final HostRepositoryDouble hostRepository = new HostRepositoryDouble();

    @BeforeEach
    void setUp() {
        service = new HostService(hostRepository);
    }

    @Test
    void shouldNotFindIA() {
        String state = "IA";
        List<Host> actual = service.findByState(state);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldFindIdaho() {
        String state = "id";
        List<Host> actual = service.findByState(state);
        assertEquals(1, actual.size());
        assertEquals("Boise", actual.get(0).getHostCity());
    }
}