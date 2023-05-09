package learn.dontwreckmyhouse.ui;

public enum Menu {
    VIEW(1, "View Reservations"),
    MAKE(2, "Make a New Reservation"),
    EDIT(3, "Edit an Existing Reservation"),
    CANCEL(4, "Cancel a Future Reservation"),
    EXIT(5, "Exit");

    private int value;
    private String title;

    public int getValue() {return value;}
    public String getTitle() {return title;}

    private Menu(int value, String title) {
        this.value = value;
        this.title = title;
    }

    public static Menu fromValue(int value) {
        for (Menu option : Menu.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }
}
