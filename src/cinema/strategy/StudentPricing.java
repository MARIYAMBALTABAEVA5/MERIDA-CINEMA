package cinema.strategy;

public class StudentPricing implements PricingStrategy {

    @Override
    public int calculate(int subtotal) {
        return subtotal * 90 / 100;
    }

    @Override
    public String label() {
        return "Student (-10%)";
    }
}
