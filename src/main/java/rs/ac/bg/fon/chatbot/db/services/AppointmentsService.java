package rs.ac.bg.fon.chatbot.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.repositories.AppointmentsRepository;

@Service("appointmentService")
public class AppointmentsService {

    @Autowired
    AppointmentsRepository appointmentsRepository;

    public Iterable<Appointment> findAll() {
        return appointmentsRepository.findAll();
    }

    public void save(Appointment appointment){
        appointmentsRepository.save(appointment);
    }
}
