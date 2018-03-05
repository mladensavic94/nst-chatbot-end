package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.SendAnswerThread;
import rs.ac.bg.fon.chatbot.db.domain.Logs;
import rs.ac.bg.fon.chatbot.db.services.LogsService;
import rs.ac.bg.fon.chatbot.db.domain.Message;

@Controller
@RequestMapping("/rest")
public class MessengerRestController {

    @Autowired
    LogsService logsService;


    @RequestMapping(value = "/chatbot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> receiveMessage(@RequestBody String json) {
        try {
            Message message = ParsingUtil.parseEventFromJson(json);
            Thread thread = new Thread(new SendAnswerThread(message));
            thread.start();
            logsService.saveLog(new Logs("LOGGING: " + json));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
