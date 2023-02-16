package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findByHost(String hostId) throws DataException {
        List<Reservation> result = reservationRepository.findByHost(hostId);
        for (Reservation reservation : result) {
            reservation.setHostId(reservation.getHostId());
            reservation.setReservationId(reservation.getReservationId());
        }
        return result;
    }

    public Result<Reservation> make(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setGuest(guestRepository.findById(reservation.getGuestId()));
        reservation.setHost(hostRepository.findById(reservation.getHostId()));
        reservation.setTotal(calculateTotal(reservation));
        result.setPayload(reservationRepository.add(reservation));
        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (reservation.getReservationId() == 0) {
            result.addErrorMessage("Invalid reservation ID.");
        }
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setTotal(calculateTotal(reservation));
        result.setPayload(reservationRepository.update(reservation));
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) throws DataException {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {return result;}
        validateFields(reservation, result);
        if (!result.isSuccess()) {return result;}
        //validateChildrenExist(reservation, result);
        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }
        if (reservation.getStart() == null) {result.addErrorMessage("Start date is required.");}
        if (reservation.getEnd() == null) {result.addErrorMessage("End date is required.");}
        if (reservation.getHostId() == null) {result.addErrorMessage("Host is required.");}
        //if (reservation.getGuest() == null) {result.addErrorMessage("Guest is required.");}
        return result;
    }

    private void validateFields(Reservation reservation, Result<Reservation> result) throws DataException {
        if (reservation.getStart().isBefore(LocalDate.now()) || reservation.getEnd().isBefore(LocalDate.now())) {
            result.addErrorMessage("Date must be today or in the future.");
        }

        LocalDate start = reservation.getStart();
        LocalDate end = reservation.getEnd();
        List<Reservation> reservations = reservationRepository.findByHost(reservation.getHostId());

        for (Reservation r : reservations) {
            LocalDate existingStart = r.getStart();
            LocalDate existingEnd = r.getEnd();
            if (reservation.getReservationId() != r.getReservationId()) {
                if (start.isEqual(existingStart)) {
                    result.addErrorMessage("Another reservation has the same start date.");
                }
                if (start.isBefore(existingStart) && end.isAfter(start)) {
                    result.addErrorMessage("The end date overlaps with another reservation.");
                }
                if (start.isAfter(existingStart) && start.isBefore(existingEnd)) {
                    result.addErrorMessage("The start date overlaps with another reservation.");
                }
            }
        }
    }

    private void validateChildrenExist(Reservation reservation, Result<Reservation> result) {

        if (reservation.getHost().getHostId() == null
                || hostRepository.findById(reservation.getHost().getHostId()) == null) {
            result.addErrorMessage("Host does not exist.");
        }

        if (guestRepository.findById(reservation.getGuest().getGuestId()) == null) {
            result.addErrorMessage("Guest does not exist.");
        }
    }

    public BigDecimal calculateTotal(Reservation reservation) {
        String hostId = reservation.getHostId();
        Host host = hostRepository.findById(hostId);
        BigDecimal standardRate = host.getStandardRate();
        BigDecimal weekendRate = host.getWeekendRate();//rates

        LocalDate start = reservation.getStart();
        LocalDate end = reservation.getEnd();
        int nights = (int)Math.abs(DAYS.between(start, end));//dates and nights

        ArrayList<LocalDate> weekNights = new ArrayList<>();
        ArrayList<LocalDate> weekendNights = new ArrayList<>();//lists to write to

        LocalDate date = start;
        for (int i = 0; i < nights; i++) {
            if (date.getDayOfWeek() != DayOfWeek.FRIDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY) {
                weekNights.add(date);
            } else {
                weekendNights.add(date);
            }
            date = date.plusDays(1);
        }

        BigDecimal weekPrice = standardRate.multiply(BigDecimal.valueOf(weekNights.size()));
        BigDecimal weekendPrice = weekendRate.multiply(BigDecimal.valueOf(weekendNights.size()));
        return weekPrice.add(weekendPrice);
    }
}