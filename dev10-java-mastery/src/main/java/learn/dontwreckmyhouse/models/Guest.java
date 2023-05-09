package learn.dontwreckmyhouse.models;

import java.math.BigDecimal;

public class Guest {
    private int guestId;
    private String guestFirstName;
    private String guestLastName;
    private String guestEmail;
    private String guestPhone;
    private String guestState;

    public int getGuestId() {
        return guestId;
    }

    public String getGuestFirstName() {
        return guestFirstName;
    }

    public String getGuestLastName() {
        return guestLastName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public String getGuestState() {
        return guestState;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setGuestFirstName(String guestFirstName) {
        this.guestFirstName = guestFirstName;
    }

    public void setGuestLastName(String guestLastName) {
        this.guestLastName = guestLastName;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public void setGuestState(String guestState) {
        this.guestState = guestState;
    }
}