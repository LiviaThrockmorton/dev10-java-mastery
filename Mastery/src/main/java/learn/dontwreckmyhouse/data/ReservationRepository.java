package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation add(Reservation reservation) throws DataException;
    boolean update(Reservation reservation) throws DataException;
    boolean cancel(String hostId, String reservationId) throws DataException;
    List<Reservation> findByHost(String hostId) throws DataException;
}
