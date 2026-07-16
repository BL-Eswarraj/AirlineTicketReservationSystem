package integration.dto;

// Represents payload sent to AWS SNS / Twilio / MSG91
public record SmsGatewayRestRequest(
        String apiKey,
        String destinationPhoneNumber,
        String messageBody,
        String senderId
) {
    public String toJsonPayload() {
        return """
                {
                  "api_key": "%s",
                  "to": "%s",
                  "sender_id": "%s",
                  "message": "%s"
                }""".formatted(apiKey, destinationPhoneNumber, senderId, messageBody);
    }
}