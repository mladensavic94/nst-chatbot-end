package rs.ac.bg.fon.chatbot.controller;

import antlr.debug.MessageEvent;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static rs.ac.bg.fon.chatbot.ParsingUtil.*;
import static rs.ac.bg.fon.chatbot.config.Constants.URL_WIT_AI;

@Service
public class ResponseService {


    private Messenger messenger;
    private ProfessorService professorService;
    private AppointmentsService appointmentsService;
    static List<Professor> professors;

    @Autowired
    public ResponseService(Messenger messenger, ProfessorService professorService, AppointmentsService appointmentsService) {
        this.messenger = messenger;
        this.professorService = professorService;
        this.appointmentsService = appointmentsService;
    }

    @Async
    public void run(TextMessageEvent messageEvent) {
        String response = null;
        try {
            response = generateAnswer(messageEvent);
            sendResponse(messageEvent.senderId(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendResponse(String sender, String text) {
        try {
            messenger.send(MessagePayload.create(sender, TextMessage.create(text)));
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private String generateAnswer(TextMessageEvent event) throws MessengerIOException {
        String appointmentString = callToWIT_AI(event.text());
        Appointment appointment = null;
        System.out.println("Response from Wit.ai: " + appointmentString);
        String response = null;
        try {
            response = parseIntent(appointmentString);
        } catch (Exception e) {
            System.out.println("Intent not parsed");
        }
        if (response != null && response.equals("request")) {
            appointment = getAppointmentForStudent(event.senderId());
            appointment.setStudentID(event.senderId());
            getUserNameAndLastName(event, appointment);
            try {
                Professor professor = findProfessorUsingLeveD(parseProfessor(appointmentString));
                try {
                    appointment.setOfficeHours(getOfficeHoursByDateForProfessor(professor, parseDate(appointmentString)));
                    response = appointment.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Date not parsed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Professor not parsed");
            }

        } else {
            response = "Jos sam prilicno glup bot, moraces da sacekas za naprednije stvari :)";
        }
        if (appointment != null) appointmentsService.save(appointment);
        return response;
    }

    private Appointment getAppointmentForStudent(String s) {
        Appointment appointment;

        appointment = appointmentsService.findByStudentID(s);

        if (appointment == null) appointment = new Appointment();

        return appointment;
    }

    private void getUserNameAndLastName(TextMessageEvent event, Appointment appointment) throws MessengerIOException {
        try {
            UserProfile userProfile = messenger.queryUserProfile(event.senderId());
            appointment.setName(userProfile.firstName() + " " + userProfile.lastName());

        } catch (MessengerApiException e) {
            e.printStackTrace();
        }
    }

    private OfficeHours getOfficeHoursByDateForProfessor(Professor professor, String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            Date date = df.parse(s);
            Optional<OfficeHours> officeHours1 = professor.getListOfOfficeHours().stream().filter(officeHours -> {
                return officeHours.getBeginTime().after(date) && officeHours.getEndTime().before(date);
            }).findFirst();
            return officeHours1.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Professor findProfessorUsingLeveD(String s) {
        if (professors == null) {
            professors = new ArrayList<>();
            professorService.findAll().forEach(professors::add);
        }
        return professors.get(0);
    }


    private String callToWIT_AI(String text) {
        RestTemplate getUserInfo = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer D5Y74IMSC5YWUFNJUC477S5GLTXGWDSG");
        headers.add("Content-type", "application/json");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> json = getUserInfo.exchange(URL_WIT_AI + text, HttpMethod.GET, requestEntity, String.class);
        return json.getBody();
    }
}
