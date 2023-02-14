package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller {
    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppMenu();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Thanks for being the Administrator!");
    }

    private void runAppMenu() throws DataException {
        Menu option;
        do {
            option = view.selectMenuOption();
            switch (option) {
                case VIEW -> viewReservations();
                case MAKE -> makeReservation();
                case EDIT -> editReservations();
                case CANCEL -> cancelReservation();
            }
        } while (option != Menu.EXIT);
    }

    private void viewReservations() throws DataException {
        view.displayHeader(Menu.VIEW.getTitle());
        String initials = view.getState();
        List<Host> hosts = hostService.findByState(initials);
        String hostId = view.chooseHost(hosts);
        if (hostId == null) {return;}
        List<Reservation> reservations = reservationService.findByHost(hostId);
        view.displayHeader("Reservations");
        view.displayReservations(reservations);
        view.enterToContinue();
    }

    private void makeReservation() {
    }

    private void editReservations() {
    }

    private void cancelReservation() {
    }
}
