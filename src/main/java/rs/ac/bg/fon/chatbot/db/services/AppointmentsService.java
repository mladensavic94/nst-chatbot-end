package rs.ac.bg.fon.chatbot.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.repositories.AppointmentsRepository;
import rs.ac.bg.fon.chatbot.db.repositories.OfficeHoursRepository;

@Service("appointmentService")
public class AppointmentsService {

    @Autowired
    AppointmentsRepository appointmentsRepository;
    @Autowired
    OfficeHoursRepository officeHoursRepository;

    public Iterable<Appointment> findAll() {
        return appointmentsRepository.findAll();
    }

    public void save(Appointment appointment){
        OfficeHours officeHours = officeHoursRepository.findOne(appointment.getOfficeHours().getId());
        appointment.setOfficeHours(officeHours);
        appointmentsRepository.save(appointment);
    }
}
