package cinema.strategy;

public class RegularPricing implements PricingStrategy {

    @Override
    public int calculate(int subtotal) {
        return subtotal;
    }

    @Override
    public String label() {
        return "Regular (0%)";
    }
}
