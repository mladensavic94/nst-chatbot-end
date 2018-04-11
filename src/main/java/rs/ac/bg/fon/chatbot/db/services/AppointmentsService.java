package rs.ac.bg.fon.chatbot.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.services.repositories.AppointmentsRepository;
import rs.ac.bg.fon.chatbot.db.services.repositories.OfficeHoursRepository;

@Service
public class AppointmentsService {

    private final
    AppointmentsRepository appointmentsRepository;
    private final
    OfficeHoursRepository officeHoursRepository;

    @Autowired
    public AppointmentsService(AppointmentsRepository appointmentsRepository, OfficeHoursRepository officeHoursRepository) {
        this.appointmentsRepository = appointmentsRepository;
        this.officeHoursRepository = officeHoursRepository;
    }

    public Iterable<Appointment> findAll() {
        return appointmentsRepository.findAll();
    }

    public Appointment save(Appointment appointment) {
        OfficeHours officeHours;
        if (appointment.getOfficeHours() != null) {
            officeHours = officeHoursRepository.findOne(appointment.getOfficeHours().getId());
            appointment.setOfficeHours(officeHours);
        }
        return appointmentsRepository.save(appointment);
    }

    public Iterable<Appointment> findAllByEmail(String email) {
        return appointmentsRepository.findAllByEmail(email);
    }


    public Appointment findByStudentID(String id) {
        Appointment appointment;

        appointment = appointmentsRepository.findByStudentID(id);

        if (appointment == null) appointment = new Appointment();

        return appointment;
    }

    public Appointment findById(Long id) {
        return appointmentsRepository.findOne(id);
    }
}
