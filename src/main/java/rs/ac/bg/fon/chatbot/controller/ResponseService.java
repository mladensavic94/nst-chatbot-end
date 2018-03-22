package rs.ac.bg.fon.chatbot.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

import java.util.HashMap;
import java.util.Map;

import static rs.ac.bg.fon.chatbot.config.Constants.URL_WIT_AI;

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
        String response = null;
        try {
            response = generateAnswer(messageEvent.text());
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

    private String generateAnswer(String text) {
        String appointment = callToWIT_AI(text);
        System.out.println("Response from Wit.ai: " + appointment);
        String response = null;
        try {
            response = parseIntent(appointment);
        } catch (Exception e) {
            System.out.println("Intent not parsed");
        }
        if (response.equals("request")) {
            try {
                response = "\n datum: " + parseDate(appointment);
            } catch (Exception e) {
                System.out.println("Date not parsed");
            }
            try {
                response += "\n profesor: " + parseProfessor(appointment);
            } catch (Exception e) {
                System.out.println("Professor not parsed");
            }
        }

        return response;
    }

    private String parseProfessor(String text) {
        String professor = null;
        if (text != null) {
            professor = ParsingUtil.getJsonObject(text, "entities");
            if (professor != null) {
                professor = ParsingUtil.getJsonArray(professor, "contact", 0);
                if (professor != null)
                    professor = ParsingUtil.getJsonField(professor, "value");
            }
        }

        return professor;

    }

    private String parseDate(String text) {
        String date = null;
        if (text != null) {
            date = ParsingUtil.getJsonObject(text, "entities");
            if (date != null) {
                date = ParsingUtil.getJsonArray(date, "datetime", 0);
                if (date != null)
                    date = ParsingUtil.getJsonField(date, "value");
            }
        }
        return date;

    }

    private String parseIntent(String text) {
        String response = null;
        if (text != null) {
            response = ParsingUtil.getJsonObject(text, "entities");
            if (response != null) {
                response = ParsingUtil.getJsonArray(response, "intent", 0);
                if (response != null)
                    response = ParsingUtil.getJsonField(response, "value");
            }
        }
        return response;

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
