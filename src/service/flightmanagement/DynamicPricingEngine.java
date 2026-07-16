package service.flightmanagement;

import domain.flightmanagement.ClassConfiguration;
import domain.flightmanagement.ManagedFlight;

public class DynamicPricingEngine {

    // 8.2 Dynamic Pricing based on Real-time Occupancy & Seasonal Factors
    public void adjustFlightPricing(ManagedFlight flight) {
        double overallOccupancy = flight.getOverallOccupancyRate();
        double seasonalMultiplier = flight.getSeasonalMultiplier();

        System.out.printf("%n[DYNAMIC PRICING ENGINE] Analyzing %s (%s -> %s) | Overall Occupancy: %.1f%% | Seasonal Factor: %.2fx%n",
                flight.getFlightNumber(), flight.getSourceAirport(), flight.getDestinationAirport(), overallOccupancy, seasonalMultiplier);

        for (ClassConfiguration config : flight.getAllClassConfigs()) {
            double classOccupancy = config.getOccupancyRate();
            double baseFare = config.getBaseFare();
            double demandMultiplier = 1.0;

            // Tiered Demand Surge / Discount Logic
            if (classOccupancy >= 85.0) {
                demandMultiplier = 1.35; // 35% surge for high scarcity
                System.out.printf(" -> [%s] High Scarcity (%.1f%% full): Applying 1.35x surge multiplier.%n", config.getTravelClass(), classOccupancy);
            } else if (classOccupancy >= 70.0) {
                demandMultiplier = 1.18; // 18% moderate surge
                System.out.printf(" -> [%s] High Demand (%.1f%% full): Applying 1.18x demand multiplier.%n", config.getTravelClass(), classOccupancy);
            } else if (classOccupancy <= 25.0) {
                demandMultiplier = 0.85; // 15% discount incentive to fill empty seats
                System.out.printf(" -> [%s] Low Occupancy (%.1f%% full): Applying 0.85x promotional discount.%n", config.getTravelClass(), classOccupancy);
            } else {
                System.out.printf(" -> [%s] Normal Demand (%.1f%% full): Standard base rate maintained.%n", config.getTravelClass(), classOccupancy);
            }

            // Calculate final dynamic price: Base * Demand Factor * Seasonal Factor
            double finalPrice = Math.round((baseFare * demandMultiplier * seasonalMultiplier) * 100.0) / 100.0;
            config.setCurrentDynamicFare(finalPrice);
        }
    }
}