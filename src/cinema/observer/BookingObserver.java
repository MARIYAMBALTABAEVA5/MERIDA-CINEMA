package cinema.observer;

import cinema.model.Booking;

public interface BookingObserver {
    void onBookingChanged(Booking booking);
}
