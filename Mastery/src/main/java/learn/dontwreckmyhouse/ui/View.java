package learn.dontwreckmyhouse.ui;

import org.springframework.stereotype.Component;

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
        String message = String.format("Select [%s-%s]: ", min, max - 1);
        return Menu.fromValue(io.readInt(message, min, max));
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }
}