package cinema.builder;

import cinema.model.*;

import java.util.*;

public class BookingBuilder {
    private String id, name, email, type, extras, discount;
    private Showtime showtime;
    private List<String> seats;
    private int total;

    public BookingBuilder id(String v) {
        id = v;
        return this;
    }

    public BookingBuilder customer(String n, String e) {
        name = n;
        email = e;
        return this;
    }

    public BookingBuilder showtime(Showtime v) {
        showtime = v;
        return this;
    }

    public BookingBuilder seats(List<String> v) {
        seats = v;
        return this;
    }

    public BookingBuilder ticketType(String v) {
        type = v;
        return this;
    }

    public BookingBuilder extras(String v) {
        extras = v;
        return this;
    }

    public BookingBuilder discount(String v) {
        discount = v;
        return this;
    }

    public BookingBuilder total(int v) {
        total = v;
        return this;
    }

    public Booking build() {
        if (id == null || name == null || name.isBlank() || email == null || email.isBlank() || showtime == null ||
                seats == null || seats.isEmpty() || type == null || extras == null || discount == null || total < 0)
            throw new IllegalStateException("Missing booking data");
        return new Booking(id, name, email, showtime, seats, type, extras, discount, total);
    }
}
