package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return result.stream().sorted(Comparator.comparing(Reservation::getStart))
                .collect(Collectors.toList());
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
        if (!result.isSuccess()) {
            return result;
        }
        if (reservation.getStart().isBefore(LocalDate.now())) {
            result.addErrorMessage("You cannot update a past reservation.");
        }
        if (result.isSuccess()) {
            if (reservationRepository.update(reservation)) {
                result.setPayload(reservation);
                reservation.setTotal(calculateTotal(reservation));
            } else {
                String message = String.format("Reservation with ID %s was not found", reservation.getReservationId());
                result.addErrorMessage(message);
            }
        }
        return result;
    }

    public Result<Reservation> cancel(Reservation reservation) throws  DataException {
        Result<Reservation> result = new Result<>();
        if (reservation.getStart().isBefore(LocalDate.now())) {
            result.addErrorMessage("You cannot cancel a past reservation.");
        }
        if (result.isSuccess()) {
            if (reservationRepository.cancel(reservation)) {
                result.setPayload(reservation);
            } else {
                String message = String.format("Reservation with ID %s was not found", reservation.getReservationId());
                result.addErrorMessage(message);
            }
        }
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) throws DataException {
        Result<Reservation> result = validateFields(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        validateDates(reservation, result);
        return result;
    }

    private Result<Reservation> validateFields(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }
        if (reservation.getStart() == null) {result.addErrorMessage("Start date is required.");}
        if (reservation.getEnd() == null) {result.addErrorMessage("End date is required.");}
        if (reservation.getHostId() == null) {result.addErrorMessage("Host is required.");}
        if (reservation.getGuestId() <= 0) {result.addErrorMessage("Guest is required.");}
        return result;
    }

    private void validateDates(Reservation reservation, Result<Reservation> result) throws DataException {
        LocalDate start = reservation.getStart();
        LocalDate end = reservation.getEnd();

        if (start.isBefore(LocalDate.now()) || end.isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation must be today or in the future.");
        }
        if (start.isAfter(end)) {
            result.addErrorMessage("Start date must be before end date");
        }

        List<Reservation> reservations = reservationRepository.findByHost(reservation.getHostId());
        for (Reservation r : reservations) {
            LocalDate existingStart = r.getStart();
            LocalDate existingEnd = r.getEnd();
            if (reservation.getReservationId() != r.getReservationId()) {
                if (start.isEqual(existingStart)) {
                    result.addErrorMessage("Another reservation has the same start date.");
                }
                if (start.isBefore(existingStart) && end.isAfter(existingStart)) {//overlaps start of existing
                    result.addErrorMessage("The end date overlaps with another reservation.");
                }
                if (start.isAfter(existingStart) && start.isBefore(existingEnd)) {//overlaps end of existing
                    result.addErrorMessage("The start date overlaps with another reservation.");
                }
                if (start.isAfter(existingStart) && end.isBefore(existingEnd)) {//new dates are within existing
                    result.addErrorMessage("The start and and dates overlap with another reservation");
                }
            }
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

        int weekNights = 0;
        int weekendNights = 0;//counters

        LocalDate date = start;
        for (int i = 0; i < nights; i++) {
            if (date.getDayOfWeek() != DayOfWeek.FRIDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY) {
                weekNights++;
            } else {
                weekendNights++;
            }
            date = date.plusDays(1);
        }

        BigDecimal weekPrice = standardRate.multiply(BigDecimal.valueOf(weekNights));
        BigDecimal weekendPrice = weekendRate.multiply(BigDecimal.valueOf(weekendNights));
        return weekPrice.add(weekendPrice).setScale(2, RoundingMode.HALF_UP);
    }
}