# Baby Pink Cinema — Ticket Reservation System

A polished Java Swing desktop application that demonstrates **six working GoF design patterns** in one cohesive cinema-booking workflow. The project is designed for a 3-person team, with two patterns per student.

## Main application features

- Five selectable movies, each with its own built-in poster-style cover.
- Day and showtime selection.
- Interactive 5 × 8 cinema seating map with available, selected, and booked states.
- Standard and VIP ticket types.
- Optional popcorn and soft-drink extras.
- Regular, Student (-10%), and Member (-15%) pricing strategies.
- Customer name and email validation.
- Live order summary and total price.
- Booking confirmation with a unique reference number.
- Double-booking prevention for the same movie/showtime/seat.
- Observer notifications printed to the console.
- Responsive centered layout with vertical scrolling only; selecting many/all seats does not create horizontal page scrolling.
- All movie cover artwork is drawn inside the Java application, so the project works offline without external image files.

## Six design patterns

| Team member | Pattern | Main files | How it is demonstrated |
|---|---|---|---|
| Student 1 | Factory | `factory/TicketFactory.java`, `StandardTicket.java`, `VipTicket.java` | Creates Standard or VIP ticket objects without exposing construction logic to the UI. |
| Student 1 | Builder | `builder/BookingBuilder.java` | Builds a complete booking step-by-step from customer, showtime, seats, ticket, extras, discount, and total. |
| Student 2 | Strategy | `strategy/*Pricing.java` | Changes the pricing algorithm at runtime: Regular, Student, or Member. |
| Student 2 | Decorator | `decorator/*Decorator.java` | Adds popcorn and drink extras to a ticket dynamically without modifying the ticket classes. |
| Student 3 | Observer | `observer/*.java` | Notifies customer/admin observers after booking events; notifications are simulated in the console. |
| Student 3 | Facade | `facade/CinemaBookingFacade.java` | Provides one simplified booking API that coordinates Factory, Decorator, Strategy, Builder, and Observer. |

## Run on Windows

1. Install **JDK 17 or newer**. Check both `java -version` and `javac -version`.
2. Extract the ZIP.
3. Double-click `run.bat`, or open Command Prompt in the project folder and run `run.bat`.

## Run with IntelliJ IDEA / VS Code

Open the project folder, then run `src/cinema/ui/CinemaApp.java`.

## Run on macOS/Linux

With JDK 17+ installed:

```bash
chmod +x run.sh
./run.sh
```

## Demo workflow for defense

1. Select a movie cover.
2. Choose a day and time.
3. Select one or more seats.
4. Change Standard/VIP, extras, and discount and show how the total updates immediately.
5. Enter a name and valid email and click **CONFIRM BOOKING**.
6. Show the booking reference and console Observer notifications.
7. Close the dialog and show that the booked seats are now disabled for that showtime.
8. Switch the time or movie and explain that seat availability is stored per `Showtime`.

## Suggested calculations to demonstrate

- 1 Standard / no extras / Regular = **2 200 KZT**.
- 1 VIP / popcorn / Student = **4 050 KZT**.
- 2 Standard / drink / Member = **4 590 KZT**.

## Final report checklist

The course requirements ask for a report containing an **Introduction, Body, Conclusion, Further Work, Java/Go code, application screenshots, and UML diagrams**, and require at least six integrated design patterns for the group. Add screenshots of the final running UI and export `docs/architecture.puml` as a UML image for the PDF report.

## Important note

This is a university demonstration application. It does not process real payments or send real email. Booking state is stored in memory and resets when the application closes.
