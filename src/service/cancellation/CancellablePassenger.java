package service.cancellation;

public class CancellablePassenger {
    private final String passengerId;
    private final String fullName;
    private final String assignedSeat;
    private final double individualFarePaid;
    private final double individualTaxPaid;
    private boolean isCancelled;

    public CancellablePassenger(String passengerId, String fullName, String assignedSeat, double individualFarePaid, double individualTaxPaid) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.assignedSeat = assignedSeat;
        this.individualFarePaid = individualFarePaid;
        this.individualTaxPaid = individualTaxPaid;
        this.isCancelled = false;
    }

    public void cancel() { this.isCancelled = true; }

    // Getters
    public String getPassengerId() { return passengerId; }
    public String getFullName() { return fullName; }
    public String getAssignedSeat() { return assignedSeat; }
    public double getIndividualFarePaid() { return individualFarePaid; }
    public double getIndividualTaxPaid() { return individualTaxPaid; }
    public boolean isCancelled() { return isCancelled; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Seat: %s | Paid: ₹%.2f | Status: %s",
                passengerId, fullName, assignedSeat, individualFarePaid, isCancelled ? "CANCELLED" : "ACTIVE");
    }
}