package cinema.strategy;

public class DisabilityPricing implements PricingStrategy {

    @Override
    public int calculate(int subtotal) {
        return (int) Math.round(subtotal * 0.80);
    }

    @Override
    public String label() {
        return "Disabilities (-20%)";
    }
}
