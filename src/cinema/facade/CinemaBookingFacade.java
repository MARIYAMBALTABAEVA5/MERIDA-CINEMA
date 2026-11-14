package cinema.facade;

import cinema.builder.BookingBuilder;
import cinema.decorator.DrinkDecorator;
import cinema.decorator.PopcornDecorator;
import cinema.factory.Ticket;
import cinema.factory.TicketFactory;
import cinema.model.Booking;
import cinema.model.Showtime;
import cinema.observer.AdminNotifier;
import cinema.observer.BookingPublisher;
import cinema.observer.CustomerNotifier;
import cinema.strategy.*;

import java.util.*;

public class CinemaBookingFacade {

    private final Map<Showtime, Set<String>> occupied = new HashMap<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final BookingPublisher publisher = new BookingPublisher();

    private int sequence = 1000;

    public CinemaBookingFacade() {
        publisher.subscribe(new CustomerNotifier());
        publisher.subscribe(new AdminNotifier());
    }

    public synchronized boolean isOccupied(Showtime showtime, String seat) {
        return occupied.getOrDefault(showtime, Set.of()).contains(seat);
    }

    public Ticket decoratedTicket(String type, boolean popcorn, boolean drink) {
        Ticket ticket = TicketFactory.create(type);

        if (popcorn) {
            ticket = new PopcornDecorator(ticket);
        }

        if (drink) {
            ticket = new DrinkDecorator(ticket);
        }

        return ticket;
    }


    private final Map<String, PricingStrategy> pricingStrategies = Map.of(
            "Regular", new RegularPricing(),
            "Student -10%", new StudentPricing(),
            "Disabilities -20%", new DisabilityPricing(),
            "Pensioners -15%", new PensionerPricing()
    );

    public PricingStrategy pricing(String type) {
        PricingStrategy strategy = pricingStrategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unknown discount type: " + type
            );
        }
        return strategy;
    }


    public int quote(String ticketType, boolean popcorn,
                     boolean drink, String discount, int seatCount) {

        if (seatCount <= 0) {
            return 0;
        }

        Ticket ticket = decoratedTicket(ticketType, popcorn, drink);
        int subtotal = ticket.getBasePrice() * seatCount;

        return pricing(discount).calculate(subtotal);
    }

    private void validateCustomer(String name, String email) {
        if (name == null || name.isBlank()
                || email == null
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "Enter a name and valid email.");
        }
    }

    private void validateSeats(Showtime showtime, List<String> seats) {
        if (showtime == null || seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("Select at least one seat.");
        }

        if (new HashSet<>(seats).size() != seats.size()) {
            throw new IllegalArgumentException("Duplicate seats selected.");
        }

        for (String seat : seats) {
            if (seat == null || !seat.matches("[A-E][1-8]")) {
                throw new IllegalArgumentException("Invalid seat: " + seat);
            }

            if (isOccupied(showtime, seat)) {
                throw new IllegalStateException(
                        "Seat " + seat + " was already booked.");
            }
        }
    }

    public synchronized Booking reserve(
            Showtime showtime,
            List<String> seats,
            String ticketType,
            boolean popcorn,
            boolean drink,
            String discount,
            String name,
            String email) {

        validateCustomer(name, email);
        validateSeats(showtime, seats);

        Ticket ticket = decoratedTicket(ticketType, popcorn, drink);
        PricingStrategy strategy = pricing(discount);

        int subtotal = ticket.getBasePrice() * seats.size();
        int total = strategy.calculate(subtotal);

        Booking booking = new BookingBuilder()
                .id("CIN-" + (++sequence))
                .customer(name.trim(), email.trim())
                .showtime(showtime)
                .seats(seats)
                .ticketType(ticketType)
                .extras(ticket.getName())
                .discount(strategy.label())
                .total(total)
                .build();

        occupied.computeIfAbsent(showtime, key -> new HashSet<>())
                .addAll(seats);

        bookings.add(booking);
        publisher.notifyAllObservers(booking);

        return booking;
    }

    public synchronized List<Booking> allBookings() {
        return List.copyOf(bookings);
    }

    public synchronized void cancel(Booking booking) {
        if (!bookings.contains(booking)
                || !"CONFIRMED".equals(booking.status())) {
            throw new IllegalStateException(
                    "Booking cannot be cancelled.");
        }

        booking.setStatus("CANCELLED");
        occupied.get(booking.showtime()).removeAll(booking.seats());

        publisher.notifyAllObservers(booking);
    }
}
