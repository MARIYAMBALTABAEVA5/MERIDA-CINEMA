# MERIDA CINEMA — Ticket Reservation System

A Java Swing desktop application demonstrating six GoF design patterns in one integrated cinema ticket reservation system.

The project was developed by a team of three students, with two design patterns implemented by each team member.

## Main Features

- Five selectable movies with poster images.
- Movie, day, and showtime selection.
- Interactive cinema seating map (5 × 8).
- Standard and VIP ticket types.
- Optional popcorn and soft drink extras.
- Different pricing strategies:
  - Regular — no discount.
  - Student — 10% discount.
  - Pensioner — 15% discount.
  - Disability — 20% discount.
- Customer name and email validation.
- Automatic order summary and total price calculation.
- Booking confirmation with a unique reference number.
- Prevention of double booking for the same movie, showtime, and seat.
- Customer and admin notifications through the Observer pattern.

## Six Design Patterns

| Team Member | Design Pattern | Main Classes |
|---|---|---|
| Mariyam | Factory | TicketFactory, StandardTicket, VipTicket |
| Mariyam | Builder | BookingBuilder |
| Dayana | Strategy | PricingStrategy, RegularPricing, StudentPricing, PensionerPricing, DisabilityPricing |
| Dayana | Observer | BookingObserver, BookingPublisher, CustomerNotifier, AdminNotifier |
| Mehrinsa | Decorator | TicketDecorator, PopcornDecorator, DrinkDecorator |
| Mehrinsa | Facade | CinemaBookingFacade |

### Pattern Integration

- **Factory:** Creates Standard and VIP ticket objects.
- **Builder:** Constructs booking objects step by step.
- **Strategy:** Applies different pricing algorithms.
- **Observer:** Sends booking notifications to customers and administrators.
- **Decorator:** Adds optional popcorn and drink extras to tickets.
- **Facade:** Coordinates the booking process by connecting the different components.

## Technologies

- Java
- Java Swing
- Object-Oriented Programming
- Git and GitHub
- IntelliJ IDEA

## Project Structure

```text
MERIDA-CINEMA/
├── docs/
│   └── architecture.puml
├── resources/
│   └── posters/
├── src/
│   └── cinema/
│       ├── builder/
│       ├── decorator/
│       ├── facade/
│       ├── factory/
│       ├── model/
│       ├── observer/
│       ├── strategy/
│       └── ui/
├── .gitignore
├── README.md
├── run.bat
└── run.sh
```

## How to Run on Windows

1. Install JDK 17 or newer.
2. Clone or download the repository.
3. Open the project folder.
4. Run `run.bat`.

## How to Run with IntelliJ IDEA

1. Open the `MERIDA-CINEMA` project.
2. Make sure JDK 17 or newer is configured.
3. Open `src/cinema/ui/CinemaApp.java`.
4. Run the `CinemaApp` class.

## Demo Workflow

1. Select a movie.
2. Choose a day and showtime.
3. Select one or more available seats.
4. Choose a Standard or VIP ticket.
5. Add popcorn or a soft drink.
6. Select a pricing strategy.
7. Enter customer details.
8. Confirm the booking and view the booking reference.
9. Check customer and admin notifications in the console.

## Important Notes

This is an educational demonstration project.

- No real payments are processed.
- No real emails are sent.
- Booking data is stored in memory and resets when the application closes.

## Team

- **Mariyam** — Factory and Builder.
- **Dayana** — Strategy and Observer.
- **Mehrinsa** — Decorator and Facade.
