package com.appointment.app.repository;

import com.appointment.app.entity.Appointment;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "appointments"; // Ensure this matches your DynamoDB table name.

    public AppointmentRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void save(Appointment appointment) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(appointment.getId()).build());
        item.put("patientName", AttributeValue.builder().s(appointment.getPatientName()).build());
        item.put("email", AttributeValue.builder().s(appointment.getEmail()).build());
        item.put("appointmentTime", AttributeValue.builder().s(appointment.getAppointmentTime()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    @Override
    public Optional<Appointment> findById(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        if (item == null || item.isEmpty()) return Optional.empty();

        Appointment appointment = new Appointment(
                item.get("id").s(),
                item.get("patientName").s(),
                item.get("email").s(),
                item.get("appointmentTime").s()
        );

        return Optional.of(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();
        List<Map<String, AttributeValue>> items = dynamoDbClient.scan(request).items();

        List<Appointment> appointments = new ArrayList<>();
        for (Map<String, AttributeValue> item : items) {
            appointments.add(new Appointment(
                    item.get("id").s(),
                    item.get("patientName").s(),
                    item.get("email").s(),
                    item.get("appointmentTime").s()
            ));
        }
        return appointments;
    }

    @Override
    public void deleteById(String id) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();

        dynamoDbClient.deleteItem(request);
    }
}
