package rs.ac.bg.fon.chatbot.controller;

import com.eclipsesource.json.JsonObject;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.Level;
import rs.ac.bg.fon.chatbot.db.domain.Message;

import java.util.HashMap;
import java.util.Map;

import static rs.ac.bg.fon.chatbot.config.Constants.*;

@Service
public class ResponseService {


    private static Map<String, Appointment> appointmentCollection;
    private Messenger messenger;

    @Autowired
    public ResponseService(Messenger messenger) {
        appointmentCollection = new HashMap<>();
        this.messenger = messenger;
    }

    @Async
    public void run(TextMessageEvent messageEvent) {
//        add(message);
//        String response = generateAnswer(message);
        sendResponse(messageEvent.senderId(), messageEvent.text());
    }

    private void sendResponse(String sender, String text) {
        try {
            messenger.send(MessagePayload.create(sender, TextMessage.create(text)));
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private String generateAnswer(Message message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.set("messaging_type", "RESPONSE");
        jsonObject.set("recipient", new JsonObject().set("id", message.getSenderID()));
        String text = parseAppointmentFromMessage((String) message.getText());
        String response = parseIntent(text);
        response += "\n datum: " + parseDate(text);
        response += "\n profesor: " + parseProfessor(text);
        jsonObject.set("message", new JsonObject().set("text", response));
        return jsonObject.toString();
    }

    private String parseProfessor(String text) {
        String professor = ParsingUtil.getJsonObject(text, "entities");
        professor = ParsingUtil.getJsonArray(professor, "contact", 0);
        professor = ParsingUtil.getJsonField(professor, "value");
        return professor;
    }

    private String parseDate(String text) {
        String date = ParsingUtil.getJsonObject(text, "entities");
        date = ParsingUtil.getJsonArray(date, "datetime", 0);
        date = ParsingUtil.getJsonField(date, "value");
        return date;
    }

    private String parseIntent(String text) {
        String response = ParsingUtil.getJsonObject(text, "entities");
        response = ParsingUtil.getJsonArray(response, "intent", 0);
        response = ParsingUtil.getJsonField(response, "value");
        return response;
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
        if (appointment == null) {
            appointment = new Appointment();
        }
//        parseAppointmentFromMessage(message.getText());

        appointmentCollection.put(message.getSenderID(), appointment);
    }

    private String parseAppointmentFromMessage(String text) {
        RestTemplate getUserInfo = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer D5Y74IMSC5YWUFNJUC477S5GLTXGWDSG");
        headers.add("Content-type", "application/json");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> json = getUserInfo.exchange(URL_WIT_AI+text, HttpMethod.GET, requestEntity, String.class);
        return json.getBody();
    }
}
