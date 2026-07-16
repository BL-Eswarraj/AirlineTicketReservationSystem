package domain.queue;

public enum BookingPriorityLevel {
    EXPRESS(1, 500.0, "Express Processing (Instant Queue Jump)"),
    REGULAR(2, 0.0, "Regular Processing (Standard Queue)");

    private final int rank; // Lower number = Higher priority
    private final double expressFee;
    private final String description;

    BookingPriorityLevel(int rank, double expressFee, String description) {
        this.rank = rank;
        this.expressFee = expressFee;
        this.description = description;
    }

    public int getRank() { return rank; }
    public double getExpressFee() { return expressFee; }
    public String getDescription() { return description; }
}