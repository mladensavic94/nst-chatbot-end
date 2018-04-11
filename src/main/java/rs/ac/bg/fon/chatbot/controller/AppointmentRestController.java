package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.Status;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/rest")
public class AppointmentRestController {

    private final AppointmentsService appointmentsService;
    private final ResponseService responseService;

    @Autowired
    public AppointmentRestController(AppointmentsService appointmentsService, ResponseService responseService) {
        this.appointmentsService = appointmentsService;
        this.responseService = responseService;
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

    @RequestMapping(value = "/appointments/update", method = RequestMethod.POST)
    public ResponseEntity<Object> updateAppointment(@RequestBody String json) {
        try {
            System.out.println(json);
            Appointment appointment = ParsingUtil.parseJsonToDomainObject(json, Appointment.class);
            Status status = appointment.getStatus();
            appointment = appointmentsService.findById(appointment.getId());
            if (appointment != null) {
                String message;
                if (status.equals(Status.ACCEPTED)) {
                    message = "Vas zahtev za konsultacije je prihvacen";
                    appointment.setStatus(Status.valueOf("ACCEPTED"));
                } else {
                    message = "Vas zahtev za konsultacije je odbijen";
                    appointment.setStatus(Status.valueOf("DENIED"));
                }
                responseService.sendResponse(appointment.getStudentID(), message);
                System.out.println(appointment);
                appointment = appointmentsService.save(appointment);
                System.out.println(appointment);
            } else throw new Exception();
            return ResponseEntity.status(HttpStatus.OK).body(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
