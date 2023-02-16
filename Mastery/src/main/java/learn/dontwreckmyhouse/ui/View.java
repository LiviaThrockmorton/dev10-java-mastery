package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class View {
    private final ConsoleIO io;
    public View(ConsoleIO io) {
        this.io = io;
    }

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
        for (Host h : hosts.stream().limit(25).toList()) {
            io.printf("%s: %s, %s %s %s%n", index++, h.getHostCity(), h.getHostState(), h.getHostEmail(), h.getHostLastName());
        }
        index--;
        if (hosts.size() > 25) {
            io.println("More than 25 hosts found, showing first 25.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);
        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1).getHostId();
    }

    //full methods for CRUD
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
        for (Reservation r : reservations) {
            io.printf("%s: %s - %s, %s, $%s%n",
                    r.getReservationId(),
                    r.getStart(),
                    r.getEnd(),
                    r.getGuestId(),
                    r.getTotal());
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