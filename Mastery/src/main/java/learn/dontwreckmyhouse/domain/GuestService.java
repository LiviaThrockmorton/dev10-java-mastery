package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> findByEmail(String email) {
        return guestRepository.findAll().stream().filter
                (i -> i.getGuestEmail().equalsIgnoreCase(email))
                .toList();
    }
}