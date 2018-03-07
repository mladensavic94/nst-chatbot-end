package rs.ac.bg.fon.chatbot.controller;

import com.eclipsesource.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.CommunicationLevelHolder;
import rs.ac.bg.fon.chatbot.db.domain.Level;
import rs.ac.bg.fon.chatbot.db.domain.Message;

import java.util.EnumSet;

import static rs.ac.bg.fon.chatbot.db.domain.Level.DATUM;

@Service
public class ResponseService {

    private final static String TOKEN = "EAAZATKY8ZBibMBAEsVy3ZA4F73jSboFAfukwl9Qa66VfFtXiAR7TTYRcSLjBjryTBZAu88j3ZAocIXyX2VdXe2EgVVUw0BdUXsiXdWEKodcr1Dh11LrUDNrTi2aTW3FKLCbVHtSRgRRR6uQJ3Jd4C47RvIibFS7wLu6xTFW6c2e9FQQ0qSsjE";
    private final static String URL = "https://graph.facebook.com/v2.6/me/messages?access_token=" + TOKEN;
    private Message message;

    @Autowired
    CommunicationLevelHolder communicationLevelHolder;


    @Async
    public void run() {
        String response = generateAnswer();
        sendResponse(response);
    }

    private void sendResponse(String response) {
        RestTemplate sendAnswer = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(response, headers);
        sendAnswer.postForLocation(URL, entity);
    }

    private String generateAnswer() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.set("messaging_type", "RESPONSE");
        jsonObject.set("recipient", new JsonObject().set("id", message.getSenderID()));
//        communicationLevelHolder.addNewCommunication(message.getSenderID(), EnumSet.of(Level.NEW));
//        Level firstMissing = communicationLevelHolder.getFirstMissing(communicationLevelHolder.getCommunicationLevel(message.getSenderID()));
        String text = (String) message.getText();
        jsonObject.set("message", new JsonObject().set("text", text));
        return jsonObject.toString();
    }

    private String getTextFromEnum(Level firstMissing) {
        switch (firstMissing){
            case DATUM: return "Kog datuma?";
            case PREDMET: return "Koji predmet?";
            case PROFESOR: return "Koji profesor?";
            default: return "";
        }
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
