
package cinema.builder;

import cinema.model.Booking;
import cinema.model.Showtime;

import java.util.List;

public class BookingBuilder {

    private String id;
    private String name;
    private String email;
    private String ticketType;
    private String extras;
    private String discount;
    private Showtime showtime;
    private List<String> seats;
    private int total;

    public BookingBuilder id(String id) {
        this.id = id;
        return this;
    }

    public BookingBuilder customer(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }

    public BookingBuilder showtime(Showtime showtime) {
        this.showtime = showtime;
        return this;
    }

    public BookingBuilder seats(List<String> seats) {
        this.seats = seats == null ? null : List.copyOf(seats);
        return this;
    }

    public BookingBuilder ticketType(String ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    public BookingBuilder extras(String extras) {
        this.extras = extras;
        return this;
    }

    public BookingBuilder discount(String discount) {
        this.discount = discount;
        return this;
    }

    public BookingBuilder total(int total) {
        this.total = total;
        return this;
    }

    public Booking build() {
        if (id == null || id.isBlank()
                || name == null || name.isBlank()
                || email == null || email.isBlank()
                || showtime == null
                || seats == null || seats.isEmpty()
                || ticketType == null
                || extras == null
                || discount == null
                || total < 0) {

            throw new IllegalStateException(
                    "Missing or invalid booking data"
            );
        }

        return new Booking(
                id, name, email, showtime, seats,
                ticketType, extras, discount, total
        );
    }
}
