package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {
    //creating dummy data
    public final static Guest GUEST = makeGuest();
    private static Guest makeGuest() {
        Guest guest = new Guest();
        guest.setGuestId(663);
        guest.setGuestFirstName("Wallis");
        guest.setGuestLastName("Kuhl");
        guest.setGuestEmail("wkuhlie@patch.com");
        guest.setGuestPhone("(704) 3740857");
        guest.setGuestState("NC");
        return guest;
    }
    private final ArrayList<Guest> guests = new ArrayList<>();
    public GuestRepositoryDouble() {guests.add(GUEST);}

    //implementing GuestRepository
    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest findById(int id) {
        return findAll().stream().filter(
                i -> i.getGuestId() == id).findFirst().orElse(null);
    }
}
