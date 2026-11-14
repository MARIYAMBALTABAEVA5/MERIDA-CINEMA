package cinema.decorator;

import cinema.factory.Ticket;

import java.util.Objects;

public abstract class TicketDecorator implements Ticket {

    protected final Ticket wrapped;

    protected TicketDecorator(Ticket wrapped) {
        this.wrapped = Objects.requireNonNull(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public int getBasePrice() {
        return wrapped.getBasePrice();
    }
}
