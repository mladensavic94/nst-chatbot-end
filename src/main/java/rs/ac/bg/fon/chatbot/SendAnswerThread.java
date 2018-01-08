package rs.ac.bg.fon.chatbot;

import com.eclipsesource.json.JsonObject;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.db.domain.Message;

public class SendAnswerThread implements Runnable {

    private final static String TOKEN = "EAAZATKY8ZBibMBAEsVy3ZA4F73jSboFAfukwl9Qa66VfFtXiAR7TTYRcSLjBjryTBZAu88j3ZAocIXyX2VdXe2EgVVUw0BdUXsiXdWEKodcr1Dh11LrUDNrTi2aTW3FKLCbVHtSRgRRR6uQJ3Jd4C47RvIibFS7wLu6xTFW6c2e9FQQ0qSsjE";
    private final static String URL = "https://graph.facebook.com/v2.6/me/messages?access_token=" + TOKEN;
    private Message message;


    public SendAnswerThread(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        String response = generateAnswer();
        System.out.println(response);
        RestTemplate sendAnswer = new RestTemplate();
        sendAnswer.postForLocation(URL, response);
    }

    private String generateAnswer() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.set("messaging_type", "RESPONSE");
        jsonObject.set("recipient", new JsonObject().set("id", message.getSenderID()));
        jsonObject.set("message", new JsonObject().set("text", message.getText().toString()));
        return jsonObject.toString();
    }
}
