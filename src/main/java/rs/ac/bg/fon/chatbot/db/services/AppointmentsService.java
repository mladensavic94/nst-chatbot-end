package rs.ac.bg.fon.chatbot.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.domain.Status;
import rs.ac.bg.fon.chatbot.db.services.repositories.AppointmentsRepository;
import rs.ac.bg.fon.chatbot.db.services.repositories.OfficeHoursRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class AppointmentsService {

    private final
    AppointmentsRepository appointmentsRepository;

    @Autowired
    public AppointmentsService(AppointmentsRepository appointmentsRepository) {
        this.appointmentsRepository = appointmentsRepository;
    }

    public Iterable<Appointment> findAll() {
        return appointmentsRepository.findAll();
    }

    public Appointment save(Appointment appointment) {
        return appointmentsRepository.save(appointment);
    }

    public Iterable<Appointment> findAllByEmail(String email) {
        return appointmentsRepository.findAllByEmail(email);
    }


    public Appointment findByStudentID(String id) {
        Appointment appointment;

        appointment = appointmentsRepository.findByStudentID(id);

        if (appointment == null) {
            appointment = new Appointment();
            appointment.setId(0L);
            appointment.setStatus(Status.OPEN);
        }

        return appointment;
    }

    public Appointment findById(Long id) {
        return appointmentsRepository.findOne(id);
    }

    public void deleteByStudentID(String s) {
        appointmentsRepository.deleteByStudentID(s);
    }

    public List<Appointment> findAllByOfficeHourId(Long id) {
       return appointmentsRepository.findAllByOfficeHourId(id);
    }

    public void updateDateTime(Appointment appointment) {
        List<Appointment> appointments = findAllByOfficeHourId(appointment.getOfficeHours().getId());
        if(appointments == null){
            appointment.setDateAndTime(appointment.getOfficeHours().getBeginTime());
        }else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(appointments.get(0).getDateAndTime());
            cal.add(Calendar.MINUTE, appointments.get(0).getLength());
            appointment.setDateAndTime(cal.getTime());
        }
    }
}
