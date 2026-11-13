package cinema.factory;

public class VipTicket implements Ticket {
    public String getName() {
        return "VIP";
    }

    public int getBasePrice() {
        return 3500;
    }
}
