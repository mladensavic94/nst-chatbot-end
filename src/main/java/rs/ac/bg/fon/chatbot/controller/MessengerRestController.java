package rs.ac.bg.fon.chatbot.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.SenderActionPayload;
import com.github.messenger4j.send.senderaction.SenderAction;
import com.github.messenger4j.webhook.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.chatbot.response.ResponseService;

import java.util.Optional;

@Controller
@RequestMapping("/rest")
@PropertySource("classpath:application.properties")
public class MessengerRestController {


    private final ResponseService responseService;
    private Messenger messenger;
    @Value("facebook.token")
    private String TOKEN;


    @Autowired
    public MessengerRestController(ResponseService responseService, Messenger messenger) {
        this.messenger = messenger;
        this.responseService = responseService;
    }


    @RequestMapping(value = "/chatbot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> receiveMessage(@RequestBody String json) {
        try {
            messenger.onReceiveEvents(json, Optional.empty(), event -> {
                sendSeen(event);
                responseService.run(event);
            });
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping("/chatbotVerification")
    public ResponseEntity<Object> validateFacebookApp(@RequestParam("hub.mode") String mode,
                               @RequestParam("hub.verify_token") String verify_token,
                               @RequestParam("hub.challenge") String challenge) {
        if (verify_token.equals(TOKEN))
            return ResponseEntity.status(HttpStatus.OK).body(challenge);
        return null;
    }

    private void sendSeen(Event event) {
        try {
            messenger.send(SenderActionPayload.create(event.senderId(), SenderAction.MARK_SEEN));
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }
}
