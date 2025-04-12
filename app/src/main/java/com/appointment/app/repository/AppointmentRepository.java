package com.appointment.app.repository;

import com.appointment.app.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    void save(Appointment appointment);
    Optional<Appointment> findById(String id);
    List<Appointment> findAll();
    void deleteById(String id);
}
