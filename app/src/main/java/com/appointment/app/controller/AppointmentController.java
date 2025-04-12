package com.appointment.app.controller;

import com.appointment.app.entity.Appointment;
import com.appointment.app.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public void createAppointment(@RequestBody Appointment appointment) {
        appointmentRepository.save(appointment);
    }
}
