package rs.ac.bg.fon.chatbot.controller;

import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.google.gson.Gson;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.chatbot.db.domain.Logs;
import rs.ac.bg.fon.chatbot.db.LogsService;

@Controller
@RequestMapping("/rest")
public class MessengerRestController {

    @Autowired
    LogsService logsService;
    @Autowired
    Genson genson;



    @RequestMapping(value = "/chatbot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> receiveMessage(@RequestBody final String json){
        try {
            TextMessageEvent event = genson.deserialize(json, TextMessageEvent.class);
            System.out.println("LOGGING: " + json + " "   + event.senderId());
            logsService.saveLog(new Logs(json));
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e){
            System.out.println("LOGGING: " + json + " " +e.getMessage() /* + event.senderId()*/);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
