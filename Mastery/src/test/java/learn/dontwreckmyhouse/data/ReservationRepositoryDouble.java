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
        reservation.setStart(LocalDate.of(2023, 10, 10));
        reservation.setEnd(LocalDate.of(2023, 10, 12));
        reservation.setHostId(HostRepositoryDouble.HOST.getHostId());
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getGuestId());//663
        reservation.setTotal(BigDecimal.valueOf(400));
        return reservation;
    }
    public final static Reservation RESERVATION2 = makeReservationTwo();
    private static Reservation makeReservationTwo() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(2);
        reservation.setStart(LocalDate.of(2023, 2, 23));
        reservation.setEnd(LocalDate.of(2023, 2, 25));
        reservation.setHostId(HostRepositoryDouble.HOST.getHostId());
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getGuestId());//663
        reservation.setTotal(BigDecimal.valueOf(450));
        return reservation;
    }
    public final static Reservation RESERVATION3 = makeReservationThree();
    private static Reservation makeReservationThree() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(3);
        reservation.setStart(LocalDate.of(2023, 2, 25));
        reservation.setEnd(LocalDate.of(2023, 2, 27));
        reservation.setHostId(HostRepositoryDouble.HOST.getHostId());
        reservation.setGuestId(GuestRepositoryDouble.GUEST.getGuestId());//663
        reservation.setTotal(BigDecimal.valueOf(450));
        return reservation;
    }
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    public ReservationRepositoryDouble() {
        reservations.add(RESERVATION);
        reservations.add(RESERVATION2);
        reservations.add(RESERVATION3);
    }

    //implementing ReservationRepository
    @Override
    public Reservation add(Reservation reservation) {
        return null;
    }

    @Override
    public Reservation update(Reservation reservation) {
        return null;
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

    @Override
    public Reservation findById(int reservationId, String hostId) throws DataException {
        return findByHost(hostId).stream().filter(
                        i -> i.getReservationId() == reservationId)
                .findFirst().orElse(null);
    }
}