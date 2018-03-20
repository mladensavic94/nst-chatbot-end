package rs.ac.bg.fon.chatbot.controller;

import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Message;
import rs.ac.bg.fon.chatbot.db.services.LogsService;

import java.util.Optional;

import static java.util.Optional.of;

@Controller
@RequestMapping("/rest")
public class MessengerRestController {


    private final ResponseService responseService;
    private Messenger messenger;

    @Autowired
    public MessengerRestController(ResponseService responseService, Messenger messenger) {
        this.messenger = messenger;
        this.responseService = responseService;
    }


    @RequestMapping(value = "/chatbot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> receiveMessage(@RequestBody String json) {
        try {
            messenger.onReceiveEvents(json, of("ja si jebi mater"), event -> {
                if(event.isTextMessageEvent()){
                    responseService.run(event.asTextMessageEvent());
                }
            });
//            Message message = ParsingUtil.parseEventFromJson(json);
//            responseService.run(message);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
