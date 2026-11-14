package cinema.decorator;

import cinema.factory.Ticket;

public class PopcornDecorator extends TicketDecorator {

    private static final int POPCORN_PRICE = 1000;

    public PopcornDecorator(Ticket wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName() + " + Popcorn combo";
    }

    @Override
    public int getBasePrice() {
        return wrapped.getBasePrice() + POPCORN_PRICE;
    }
}
