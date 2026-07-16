package integration.dto;

public record SmsGatewayRestResponse(
        int httpStatusCode,
        String messageId,
        String deliveryStatus,
        double costInr
) {}