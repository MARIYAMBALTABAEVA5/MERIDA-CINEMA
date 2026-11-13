package cinema.strategy;

public class PensionerPricing implements PricingStrategy {

    @Override
    public int calculate(int subtotal) {
        return (int) Math.round(subtotal * 0.85);
    }

    @Override
    public String label() {
        return "Pensioners (-15%)";
    }
}
