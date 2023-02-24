package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class View {
    private final ConsoleIO io;
    public View(ConsoleIO io) {
        this.io = io;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Menu selectMenuOption() {
        displayHeader("Administrator Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Menu option : Menu.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getTitle());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }
        String message = String.format("Select [%s-%s]: ", min, max);
        return Menu.fromValue(io.readInt(message, min, max));
    }

    public String getState() {
        return io.readRequiredString("State abbreviation: ");
    }

    public String getCity() {return io.readRequiredString("City: ");
    }

    public String getEmail() {return io.readRequiredString("Enter email: "); }

    public LocalDate getDate() {return io.readLocalDate("Enter in MM/dd/yyyy format: "); }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public int getGuest(List<Guest> guest) {
        if (guest.size() == 0) {
            io.println("No guests found.");
            return 0;
        }
        return guest.get(0).getGuestId();
    }

    public String chooseHost(List<Host> hosts) {
        if (hosts.size() == 0) {
            io.println("No hosts found.");
            return null;
        }
        int index = 1;
        for (Host h : hosts) {
            io.printf("%s: %s, %s %s %s%n", index++, h.getHostCity(), h.getHostState(), h.getHostEmail(), h.getHostLastName());
        }

        io.println("0: Exit");
        index--;
        String message = String.format("Select host by index [0-%s]: ", index);
        index = io.readInt(message, 0, index);
        if (index <= 0) {
            displayStatus(false, String.format("No hosts at index %s.", index));
            return null;
        }
        return hosts.get(index - 1).getHostId();
    }

    public Reservation chooseReservation(List<Reservation> reservations) {
        displayHeader("Choose Reservation");
        if (reservations.size() == 0) {return null;}
        displayReservations(reservations);
        io.println("0: Exit");
        String message = String.format("Select reservation by index [0-%s]: ", reservations.size());

        int index = io.readInt(message, 0, reservations.size());
        int reservationId = reservations.get(index).getReservationId();
        Reservation reservation = reservations.stream().filter(
                r -> r.getReservationId() == reservationId)
                .findFirst().orElse(null);
        if (reservation == null) {
            displayStatus(false, String.format(
                    "No reservation with ID %s found for this host.", reservationId));
        }
        return reservations.get(index - 1);
    }

    //CRUD
    public Reservation makeReservation(int guestId, String hostId) {
        displayHeader("Choose Start Date");
        LocalDate start = getDate();

        displayHeader("Choose End Date");
        LocalDate end = getDate();
        Reservation r = new Reservation();
        r.setStart(start);
        r.setEnd(end);
        r.setGuestId(guestId);
        r.setHostId(hostId);
        return r;
    }

    public void update(Reservation reservation) {
        LocalDate start = io.readLocalDateOptional("Start Date (" + formatter.format(reservation.getStart()) + "): ");
        if (start != null) {
            reservation.setStart(start);
        }

        LocalDate end = io.readLocalDateOptional("End Date (" + formatter.format(reservation.getEnd()) + "): ");
        if (end != null) {
            reservation.setEnd(end);
        }
    }

    public boolean cancel(Reservation r) {
        displayHeader("Confirmation");
        io.printf("Reservation ID: %s, Guest ID: %s, %s - %s, $%s%n",
                r.getReservationId(),
                r.getGuestId(),
                formatter.format(r.getStart()),
                formatter.format(r.getEnd()),
                r.getTotal().setScale(2, RoundingMode.HALF_UP));
        String prompt = String.format("Are you sure you want to cancel reservation with ID: %s? [y/n]: ", r.getReservationId());
        return io.readBoolean(prompt);
    }


    //displays
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayReservations(List<Reservation> reservations) {
        if (reservations.size() == 0) {
            io.println("No reservations for this host.");
        }
        int index = 1;
        for (Reservation r : reservations) {
            io.printf("%s: Reservation ID: %s, Guest ID: %s, %s - %s, $%s%n",
                    index++,
                    r.getReservationId(),
                    r.getGuestId(),
                    formatter.format(r.getStart()),
                    formatter.format(r.getEnd()),
                    r.getTotal().setScale(2, RoundingMode.HALF_UP));
        }
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }
}