package cinema.observer;

import cinema.model.Booking;
import java.util.ArrayList;
import java.util.List;

public class BookingPublisher {

    private final List<BookingObserver> observers = new ArrayList<>();

    public void subscribe(BookingObserver observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingChanged(booking);
        }
    }
}

