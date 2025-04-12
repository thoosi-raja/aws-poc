package com.appointment.app.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import java.util.UUID;

@DynamoDbBean
public class Appointment {

    private String id;
    private String patientName;
    private String email;
    private String appointmentTime;

    public Appointment() {
        this.id = UUID.randomUUID().toString(); // Generate unique ID
    }

    public Appointment(String id, String patientName, String email, String appointmentTime) {
        this.id = id;
        this.patientName = patientName;
        this.email = email;
        this.appointmentTime = appointmentTime;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("patientName")
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @DynamoDbAttribute("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDbAttribute("appointmentTime")
    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
