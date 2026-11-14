package cinema.decorator;

import cinema.factory.Ticket;

public class DrinkDecorator extends TicketDecorator {

    private static final int DRINK_PRICE = 500;

    public DrinkDecorator(Ticket wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName() + " + Drink";
    }

    @Override
    public int getBasePrice() {
        return wrapped.getBasePrice() + DRINK_PRICE;
    }
}
