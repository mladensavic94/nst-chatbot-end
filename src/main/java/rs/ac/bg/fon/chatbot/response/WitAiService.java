package rs.ac.bg.fon.chatbot.response;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class WitAiService implements NLPService{

    private static String WIT_AI_VERSION = "20180320";
    private final static String URL_WIT_AI = "https://api.wit.ai/message?v=" + WIT_AI_VERSION + "&q=";
    private String token = "Bearer D5Y74IMSC5YWUFNJUC477S5GLTXGWDSG";

    static {
        WIT_AI_VERSION = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public String callAndParse(String text) {
        RestTemplate getUserInfo = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, token);
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> json = getUserInfo.exchange(URL_WIT_AI + text, HttpMethod.GET, requestEntity, String.class);
        return json.getBody();
    }

}
