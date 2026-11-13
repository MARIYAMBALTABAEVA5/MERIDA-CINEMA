package cinema.model;

public record Showtime(Movie movie, String day, String time, String hall) {
    @Override
    public String toString() {
        return day + " • " + time + " • " + hall;
    }
}
