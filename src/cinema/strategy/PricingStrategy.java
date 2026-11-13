package cinema.strategy;

public interface PricingStrategy {
    int calculate(int subtotal);

    String label();
}
