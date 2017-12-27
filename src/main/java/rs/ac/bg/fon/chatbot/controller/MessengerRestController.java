package rs.ac.bg.fon.chatbot.controller;

import com.github.messenger4j.webhook.Event;
import com.google.gson.Gson;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public @ResponseBody Object receiveMessage(Object json){
        try {
        //    Event event = gson.fromJson(gson.toJson(json), Event.class);
            System.out.println("LOGGING: " + json + " "  /* + event.senderId()*/);
            logsService.saveLog(new Logs(genson.serialize(json)));
            return ResponseEntity.status(HttpStatus.OK).body("Radi " + json);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body("Radi kurac " + genson.serialize(json));
        }
    }
}
