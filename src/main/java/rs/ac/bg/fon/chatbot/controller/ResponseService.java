package rs.ac.bg.fon.chatbot.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
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
import rs.ac.bg.fon.chatbot.db.domain.Status;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;
import rs.ac.bg.fon.chatbot.db.services.OfficeHoursService;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static rs.ac.bg.fon.chatbot.ParsingUtil.*;
import static rs.ac.bg.fon.chatbot.config.Constants.URL_WIT_AI;

@Service
public class ResponseService {


    private Messenger messenger;
    private ProfessorService professorService;
    private AppointmentsService appointmentsService;
    private OfficeHoursService officeHoursService;

    @Autowired
    public ResponseService(Messenger messenger, ProfessorService professorService, AppointmentsService appointmentsService, OfficeHoursService officeHoursService) {
        this.messenger = messenger;
        this.professorService = professorService;
        this.appointmentsService = appointmentsService;
        this.officeHoursService = officeHoursService;
    }


    @Async
    void run(TextMessageEvent messageEvent) {
        try {
            MessagePayload response = generateAnswer(messageEvent.senderId(), messageEvent.text());
            sendResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Async
    void run(QuickReplyMessageEvent messageEvent) {
        try {
            MessagePayload response = generateAnswer(messageEvent.senderId(), messageEvent.text());
            sendResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void sendResponse(MessagePayload message) {
        try {
            messenger.send(message);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private MessagePayload generateAnswer(String senderId, String text) throws MessengerIOException {
        if (text.equalsIgnoreCase("restart")) {
            appointmentsService.deleteByStudentID(senderId);
            return MessagePayload.create(senderId, TextMessage.create("Stanje obrisano, mozete poceti iz pocetka"));
        }
        String appointmentString = callToWIT_AI(text);
        Appointment appointment;
        System.out.println("Response from Wit.ai: " + appointmentString);
        TextMessage response;
        String intent = getResponseBasedOnIntent(appointmentString);
        appointment = appointmentsService.findByStudentID(senderId);
        if ((intent != null && intent.equals("request")) || appointment.getId() != 0) {
            appointment.setStudentID(senderId);
            getUserNameAndLastName(senderId, appointment);
            response = getResponseBasedOnProfessorParameter(appointmentString, appointment);
            TextMessage pom = getResponseBasedOnDateParameter(appointmentString, appointment);
            if (pom != null) {
                response = pom;
            }
            appointmentsService.save(appointment);
            if (appointment.getStatus().equals(Status.FULL)) {
                response = TextMessage.create("Zahtev za konsultacije poslat profesoru na odobrenje\nProfesor: " + appointment.getProfessor().getLastName() + " " + appointment.getProfessor().getFirstName() + "\nDatum: " + appointment.getDateAndTime());
            }

        } else {
            response = TextMessage.create("Jos sam prilicno glup bot, moraces da sacekas za naprednije stvari :)");
        }

        return MessagePayload.create(senderId, response);
    }

    private TextMessage getResponseBasedOnDateParameter(String appointmentString, Appointment appointment) {
        TextMessage response = null;
        try {
            Date date = parseDate(appointmentString);
            OfficeHours officeHours = officeHoursService.filterByDate(date, appointment.getProfessor());
            if (officeHours == null) {
                String text = "U tom terminu nema konsultacija.";
                List<QuickReply> quickReplies = new ArrayList<>();
                officeHoursService.findAllByProfessorId(appointment.getProfessor().getEmail())
                        .forEach(officeHours1 -> {
                            if(officeHours1.getBeginTime().after(new Date()))
                                quickReplies.add(TextQuickReply.create(officeHours1.getBeginTime().toString(), officeHours1.getBeginTime().toString()));
                        });
                response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
            }
            appointment.setDateAndTime(date);
            appointment.setOfficeHours(officeHours);
        } catch (Exception e) {
            System.out.println("Date not parsed " +e.getMessage());

        }
        return response;
    }

    private TextMessage getResponseBasedOnProfessorParameter(String appointmentString, Appointment appointment) {
        Professor professor;
        TextMessage response = null;
        try {
            String professorString = parseProfessor(appointmentString);
            professor = professorService.findProfessorUsingStringDistance(professorString);
            if (professor != null) {
                appointment.setProfessor(professor);
                String text = "Kog dana zelite kod prof. " + professor.getLastName() + " na konsultacije";
                List<QuickReply> quickReplies = new ArrayList<>();
                professor.getListOfOfficeHours().stream()
                        .filter(officeHours -> officeHours.getBeginTime().after(new Date()))
                        .forEach(officeHours ->
                                quickReplies.add(TextQuickReply.create(officeHours.getBeginTime().toString(), "<POSTBACK_PAYLOAD>")));
                response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
            } else {
                String text = "Profesor koga trazite ne postoji u sistemu.\n Neki od ponudjenih su dati u nastavku:";
                List<QuickReply> quickReplies = new ArrayList<>();
                professorService.findAll().forEach(professor1 -> quickReplies.add(TextQuickReply.create(professor1.getLastName() + " " + professor1.getFirstName(), professor1.getLastName() + " " + professor1.getFirstName())));
                appointment.setProfessor(null);
                response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
            }
        } catch (Exception e) {
            e.printStackTrace();
            String text = "Kod kog profesora zelite na konsultacije?";
            List<QuickReply> quickReplies = new ArrayList<>();
            professorService.findAll().forEach(professor1 -> quickReplies.add(TextQuickReply.create(professor1.getLastName() + " " + professor1.getFirstName(), professor1.getLastName() + " " + professor1.getFirstName())));
            appointment.setProfessor(null);
            response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
        }
        return response;
    }

    private String getResponseBasedOnIntent(String appointmentString) {
        String response = null;
        try {
            response = parseIntent(appointmentString);
        } catch (Exception e) {
            System.out.println("Intent not parsed");
        }
        return response;
    }

    private void getUserNameAndLastName(String senderId, Appointment appointment) throws MessengerIOException {
        try {
            UserProfile userProfile = messenger.queryUserProfile(senderId);
            appointment.setName(userProfile.firstName() + " " + userProfile.lastName());

        } catch (MessengerApiException e) {
            e.printStackTrace();
        }
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
