package manager;

public class SystemConfigurationManager {
    private String airlineName;
    private String defaultCurrency;
    private double defaultTaxRatePercentage;
    private double expressQueueFee;
    private boolean isMaintenanceModeActive;
    private String systemSupportEmail;

    // Private constructor
    private SystemConfigurationManager() {
        System.out.println("[CONFIG MANAGER INIT] Loading global airline system configurations...");
        this.airlineName = "SRM Global Airways";
        this.defaultCurrency = "INR (₹)";
        this.defaultTaxRatePercentage = 18.0; // 18% GST standard
        this.expressQueueFee = 500.0;
        this.isMaintenanceModeActive = false;
        this.systemSupportEmail = "support@srmairways.com";
    }

    // Bill Pugh Static Inner Class - Loaded only when getInstance() is called
    private static class ConfigurationHolder {
        private static final SystemConfigurationManager INSTANCE = new SystemConfigurationManager();
    }

    public static SystemConfigurationManager getInstance() {
        return ConfigurationHolder.INSTANCE;
    }

    // Global Operational Check
    public void validateSystemAvailability() {
        if (isMaintenanceModeActive) {
            throw new IllegalStateException("CRITICAL: System is currently under scheduled maintenance. Booking portal temporarily disabled.");
        }
    }

    // Getters and Setters for Global Configurations
    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }
    public String getDefaultCurrency() { return defaultCurrency; }
    public double getDefaultTaxRatePercentage() { return defaultTaxRatePercentage; }
    public void setDefaultTaxRatePercentage(double taxRate) { this.defaultTaxRatePercentage = taxRate; }
    public double getExpressQueueFee() { return expressQueueFee; }
    public boolean isMaintenanceModeActive() { return isMaintenanceModeActive; }
    public void setMaintenanceModeActive(boolean active) {
        this.isMaintenanceModeActive = active;
        System.out.printf("[SYSTEM ALERT] Maintenance Mode toggled to: %b%n", active);
    }
    public String getSystemSupportEmail() { return systemSupportEmail; }

    @Override
    public String toString() {
        return String.format("Airline: %s | Currency: %s | GST Tax: %.1f%% | Express Fee: ₹%.2f | Maintenance: %b | Support: %s",
                airlineName, defaultCurrency, defaultTaxRatePercentage, expressQueueFee, isMaintenanceModeActive, systemSupportEmail);
    }
}