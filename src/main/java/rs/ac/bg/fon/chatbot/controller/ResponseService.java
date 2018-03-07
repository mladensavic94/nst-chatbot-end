package rs.ac.bg.fon.chatbot.controller;

import com.eclipsesource.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.CommunicationLevelHolder;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.Level;
import rs.ac.bg.fon.chatbot.db.domain.Message;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static rs.ac.bg.fon.chatbot.db.domain.Level.DATUM;

@Service
public class ResponseService {

    private final static String TOKEN = "EAAZATKY8ZBibMBAEsVy3ZA4F73jSboFAfukwl9Qa66VfFtXiAR7TTYRcSLjBjryTBZAu88j3ZAocIXyX2VdXe2EgVVUw0BdUXsiXdWEKodcr1Dh11LrUDNrTi2aTW3FKLCbVHtSRgRRR6uQJ3Jd4C47RvIibFS7wLu6xTFW6c2e9FQQ0qSsjE";
    private final static String URL = "https://graph.facebook.com/v2.6/me/messages?access_token=" + TOKEN;
    private Message message;
    private static Map<String, Appointment> appointmentCollection;

    static {
        appointmentCollection = new HashMap<>();
    }


    @Autowired
    CommunicationLevelHolder communicationLevelHolder;


    @Async
    public void run(Message message) {
        add(message);
        String response = generateAnswer(message);
        sendResponse(response);
    }

    private void sendResponse(String response) {
        RestTemplate sendAnswer = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(response, headers);
        sendAnswer.postForLocation(URL, entity);
    }

    private String generateAnswer(Message message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.set("messaging_type", "RESPONSE");
        jsonObject.set("recipient", new JsonObject().set("id", message.getSenderID()));
        String text = parseAppointmentFromMessage(message);
        jsonObject.set("message", new JsonObject().set("text", text));
        return jsonObject.toString();
    }

    private String getTextFromEnum(Level firstMissing) {
        switch (firstMissing) {
            case DATUM:
                return "Kog datuma?";
            case PREDMET:
                return "Koji predmet?";
            case PROFESOR:
                return "Koji profesor?";
            default:
                return "";
        }
    }

    public void add(Message message) {
        Appointment appointment = appointmentCollection.get(message.getSenderID());
        if(appointment == null){
            appointment = new Appointment();
        }
        parseAppointmentFromMessage(message.getText());

        appointmentCollection.put(message.getSenderID(), appointment);
    }

    private String parseAppointmentFromMessage(Object text) {
        RestTemplate getUserInfo = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer D5Y74IMSC5YWUFNJUC477S5GLTXGWDSG");
        HttpEntity<?> requestEntity = new HttpEntity<>(text, headers);
        ResponseEntity<String> json = getUserInfo.exchange(URL, HttpMethod.GET, requestEntity, String.class);
        return json.getBody();
    }
}
