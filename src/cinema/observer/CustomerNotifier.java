package cinema.observer;

import cinema.model.Booking;

public class CustomerNotifier implements BookingObserver {

    @Override
    public void onBookingChanged(Booking booking) {
        System.out.println(
                "[Customer notification DEMO] "
                        + booking.email()
                        + " -> "
                        + booking.id()
                        + ": "
                        + booking.status()
        );
    }
}
