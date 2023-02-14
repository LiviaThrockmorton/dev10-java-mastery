package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import org.springframework.stereotype.Component;

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

    private void viewReservations() {
    }

    private void makeReservation() {
    }

    private void editReservations() {
    }

    private void cancelReservation() {
    }
}
