package rs.ac.bg.fon.chatbot.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Event;
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



    @RequestMapping(value = "/chatbot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object receiveMessage(String json){
        Event event = new Gson().fromJson(json, Event.class);
        logsService.saveLog(new Logs(json));
        return ResponseEntity.status(HttpStatus.OK).body("Radi " + json);
    }
}
