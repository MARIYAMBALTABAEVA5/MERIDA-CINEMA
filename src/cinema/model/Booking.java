package cinema.model;

import java.util.*;

public final class Booking {
    private final String id, customerName, email;
    private final Showtime showtime;
    private final List<String> seats;
    private final String ticketType, extras, discount;
    private final int total;
    private String status = "CONFIRMED";

    public Booking(String id, String customerName, String email, Showtime showtime, List<String> seats,
                   String ticketType, String extras, String discount, int total) {
        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.showtime = showtime;
        this.seats = List.copyOf(seats);
        this.ticketType = ticketType;
        this.extras = extras;
        this.discount = discount;
        this.total = total;
    }

    public String id() {
        return id;
    }

    public String customerName() {
        return customerName;
    }

    public String email() {
        return email;
    }

    public Showtime showtime() {
        return showtime;
    }

    public List<String> seats() {
        return seats;
    }

    public String ticketType() {
        return ticketType;
    }

    public String extras() {
        return extras;
    }

    public String discount() {
        return discount;
    }

    public int total() {
        return total;
    }

    public String status() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
