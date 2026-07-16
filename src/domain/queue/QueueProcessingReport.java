package domain.queue;

public record QueueProcessingReport(
        int totalProcessed,
        int expressProcessedCount,
        int regularProcessedCount,
        int starvationElevatedCount,
        double totalExpressFeeRevenue,
        double averageWaitTimeMillis,
        double averageProcessingTimeMillis
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=========================================================================\n");
        sb.append("                 PRIORITY QUEUE PROCESSING ANALYTICS REPORT              \n");
        sb.append("=========================================================================\n");
        sb.append(String.format(" Total Requests Processed  : %d%n", totalProcessed));
        sb.append(String.format("  -> Express Bookings      : %d (Included %d Starvation Elevated)%n", expressProcessedCount, starvationElevatedCount));
        sb.append(String.format("  -> Regular Bookings      : %d%n", regularProcessedCount));
        sb.append("-------------------------------------------------------------------------\n");
        sb.append(String.format(" Total Express Fee Revenue : ₹%.2f%n", totalExpressFeeRevenue));
        sb.append(String.format(" Average Queue Wait Time   : %.2f ms%n", averageWaitTimeMillis));
        sb.append(String.format(" Average Processing Time   : %.2f ms%n", averageProcessingTimeMillis));
        sb.append("=========================================================================\n");
        return sb.toString();
    }
}