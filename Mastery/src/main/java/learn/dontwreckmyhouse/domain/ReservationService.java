package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
    }

    public List<Reservation> findByHost(String hostId) throws DataException {
        List<Reservation> result = reservationRepository.findByHost(hostId);
        for (Reservation reservation : result) {
            reservation.setHostId(reservation.getHostId());
            reservation.setReservationId(reservation.getReservationId());
        }
        return result;
    }
}
