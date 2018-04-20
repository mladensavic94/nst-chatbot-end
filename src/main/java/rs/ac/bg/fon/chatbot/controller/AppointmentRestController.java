package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.Status;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;

import javax.persistence.EntityManager;

@Controller
@RequestMapping("/rest")
public class AppointmentRestController {

    private final AppointmentsService appointmentsService;
    private final ResponseService responseService;
    private final EntityManager entityManager;

    @Autowired
    public AppointmentRestController(AppointmentsService appointmentsService, ResponseService responseService, EntityManager entityManager) {
        this.appointmentsService = appointmentsService;
        this.responseService = responseService;
        this.entityManager = entityManager;
    }

    @RequestMapping(value = "/appointments", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllAppointments(@RequestParam String email) {
        try {
            Iterable<Appointment> serviceAllByEmail = appointmentsService.findAllByEmail(email);
            String response = ParsingUtil.parseListToJson(serviceAllByEmail);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/appointments/save", method = RequestMethod.POST)
    public ResponseEntity<Object> saveAppointment(@RequestBody String json) {
        try {
            Appointment appointment = ParsingUtil.parseJsonToDomainObject(json, Appointment.class);
            appointmentsService.save(appointment);
            return ResponseEntity.status(HttpStatus.OK).body(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/appointments/update", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseEntity<Object> updateAppointment(@RequestBody String json) {
        try {
            System.out.println(json);
            Appointment appointment = ParsingUtil.parseJsonToDomainObject(json, Appointment.class);
            Status status = appointment.getStatus();
            appointment = appointmentsService.findById(appointment.getId());
            if (appointment != null) {
                String message;
                if (status.equals(Status.ACCEPTED)) {
                    message = "Vas zahtev za konsultacije je prihvacen!\nDetalji: -> datum " + appointment.getDateAndTime()+ " profesor: " +appointment.getProfessor().getLastName() + " " + appointment.getProfessor().getFirstName();
                    appointment.setStatus(Status.valueOf("ACCEPTED"));
                } else {
                    message = "Vas zahtev za konsultacije je odbijen!\nDetalji: -> datum " + appointment.getDateAndTime()+ " profesor: " +appointment.getProfessor().getLastName() + " " + appointment.getProfessor().getFirstName();
                    appointment.setStatus(Status.valueOf("DENIED"));
                }
                responseService.sendResponse(appointment.getStudentID(), message);
                System.out.println(appointment);
                appointment = appointmentsService.save(appointment);
                entityManager.clear();
            } else throw new Exception();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
