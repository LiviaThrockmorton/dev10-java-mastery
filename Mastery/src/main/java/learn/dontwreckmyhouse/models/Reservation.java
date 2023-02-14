package learn.dontwreckmyhouse.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {
    private String reservationId;
    private LocalDate start;
    private LocalDate end;
    private String hostId;
    private String guestId;
    private BigDecimal total;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public BigDecimal getTotal() {return total;}

    public void setTotal(BigDecimal total) {this.total = total;}
}
