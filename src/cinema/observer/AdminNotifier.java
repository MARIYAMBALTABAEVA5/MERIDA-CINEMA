package cinema.observer;

import cinema.model.Booking;

public class AdminNotifier implements BookingObserver {

    @Override
    public void onBookingChanged(Booking booking) {
        System.out.println(
                "[Admin notification DEMO] Booking "
                        + booking.id()
                        + " status: "
                        + booking.status()
        );
    }
}
