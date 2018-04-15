package rs.ac.bg.fon.chatbot.controller;

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
import rs.ac.bg.fon.chatbot.db.domain.Status;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;
import rs.ac.bg.fon.chatbot.db.services.OfficeHoursService;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

import java.util.Date;

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
    public void run(TextMessageEvent messageEvent) {
        try {
            //TODO generate message payload with quick reply
            String response = generateAnswer(messageEvent);
            sendResponse(messageEvent.senderId(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void sendResponse(String sender, String text) {
        try {
            messenger.send(MessagePayload.create(sender, TextMessage.create(text)));
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private String generateAnswer(TextMessageEvent event) throws MessengerIOException {
        String appointmentString = callToWIT_AI(event.text());
        Appointment appointment;
        System.out.println("Response from Wit.ai: " + appointmentString);
        String response;
        response = getResponseBasedOnIntent(appointmentString);
        appointment = appointmentsService.findByStudentID(event.senderId());
        if ((response != null && response.equals("request")) || appointment.getId() != 0) {
            appointment.setStudentID(event.senderId());
            getUserNameAndLastName(event, appointment);
            response = getResponseBasedOnProfessorParameter(appointmentString, appointment);
            String dateMessage = getResponseBasedOnDateParameter(appointmentString, appointment);
            if(dateMessage != null){
                response = dateMessage;
            }
            appointmentsService.save(appointment);
            if (appointment.getStatus().equals(Status.FULL)) {
                response = "Zahtev za konsultacije poslat profesoru na odobrenje";
                response += "\nProfesor: "+ appointment.getProfessor().getLastName() + " " + appointment.getProfessor().getFirstName() + "\nDatum: " + appointment.getDateAndTime();
            }

        } else {
            response = "Jos sam prilicno glup bot, moraces da sacekas za naprednije stvari :)";
        }

        return response;
    }

    private String getResponseBasedOnDateParameter(String appointmentString, Appointment appointment) {
        String response = null;
        try {
            Date date = parseDate(appointmentString);
            OfficeHours officeHours = officeHoursService.filterByDate(date, appointment.getProfessor());
            if (officeHours == null){
                officeHours = officeHoursService.findAllByProfessorId(appointment.getProfessor().getEmail()).iterator().next();
                response = "U tom terminu nema konsultacija. Trenutno aktivne su u periodu od " + officeHours.getBeginTime() + " do " + officeHours.getEndTime()+ ".";

            }
            //Ovde bi trebalo da kazem kad ima!
            appointment.setDateAndTime(date);
            appointment.setOfficeHours(officeHours);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Date not parsed");
        }
        return response;
    }

    private String getResponseBasedOnProfessorParameter(String appointmentString, Appointment appointment) {
        Professor professor;
        String response;
        try {
            String professorString = parseProfessor(appointmentString);
            professor = professorService.findProfessorUsingStringDistance(professorString);
            if(professor != null){
                appointment.setProfessor(professor);
                response = "Kog dana zelite kod prof. " + professor.getLastName() + " na konsultacije";
            }else{
                response = "Profesor koga trazite ne postoji u sistemu.";
                appointment.setProfessor(null);
            }
        } catch (Exception e) {
                e.printStackTrace();
            response = "Kod kog profesora zelite na konsultacije?";
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

    private void getUserNameAndLastName(TextMessageEvent event, Appointment appointment) throws MessengerIOException {
        try {
            UserProfile userProfile = messenger.queryUserProfile(event.senderId());
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
