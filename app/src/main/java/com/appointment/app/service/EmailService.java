package com.appointment.app.service;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final SesClient sesClient;

    @Value("${aws.ses.fromEmail}")
    private String fromEmail;

    public EmailService() {
        // Initialize the SES client using the default AWS credentials
        this.sesClient = SesClient.create();
    }

    public void sendOrderConfirmation(String toEmail, String customerName, List<String> items, double total) {
        try {
            // Construct the email subject and body
            String subject = "Order Confirmation - Your E-commerce Order";
            String body = buildOrderConfirmationEmail(customerName, items, total);

            // Create the email request
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(Destination.builder().toAddresses(toEmail).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder().html(Content.builder().data(body).build()).build())
                            .build())
                    .build();

            // Send the email using SES
            sesClient.sendEmail(request);
            logger.info("Order confirmation email sent successfully to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email to {}: {}", toEmail, e.getMessage());
            // Log the error, but do not throw an exception
        }
    }

    private String buildOrderConfirmationEmail(String customerName, List<String> items, double total) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Thank you for your order, ").append(customerName).append("!</h2>");
        html.append("<p>Your order has been confirmed. Here are the details:</p>");
        html.append("<h3>Order Items:</h3>");
        html.append("<ul>");
        for (String item : items) {
            html.append("<li>").append(item).append("</li>");
        }
        html.append("</ul>");
        html.append("<p><strong>Total Amount: $").append(String.format("%.2f", total)).append("</strong></p>");
        html.append("<p>We will process your order shortly.</p>");
        html.append("<p>Best regards,<br>Your E-commerce Team</p>");
        html.append("</body></html>");
        return html.toString();
    }
}
