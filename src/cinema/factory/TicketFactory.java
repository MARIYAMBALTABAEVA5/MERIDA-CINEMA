package cinema.factory;

public final class TicketFactory {
    private TicketFactory() {
    }

    public static Ticket create(String type) {
        return switch (type) {
            case "Standard" -> new StandardTicket();
            case "VIP" -> new VipTicket();
            default -> throw new IllegalArgumentException("Unknown ticket type: " + type);
        };
    }
}
