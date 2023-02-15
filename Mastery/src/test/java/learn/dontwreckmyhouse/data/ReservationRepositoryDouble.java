package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {
    //making dummy data
    public final static Reservation RESERVATION = makeReservation();
    private static Reservation makeReservation() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setStart(LocalDate.ofEpochDay(2023-10-10));
        reservation.setEnd(LocalDate.ofEpochDay(2023-10-12));
        reservation.setHostId(HostRepositoryDouble.HOST.getHostId());
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getGuestId());//663
        reservation.setTotal(BigDecimal.valueOf(400));
        return reservation;
    }
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    public ReservationRepositoryDouble() {reservations.add(RESERVATION);}

    //implementing ReservationRepository
    @Override
    public Reservation add(Reservation reservation) {
        return null;
    }

    @Override
    public boolean update(Reservation reservation) {
        return false;
    }

    @Override
    public boolean cancel(String hostId, String reservationId) {
        return false;
    }

    @Override
    public List<Reservation> findByHost(String hostId) {
        return reservations.stream()
                .filter(r -> r.getHostId().equals(hostId))
                .collect(Collectors.toList());
    }
}