package rs.ac.bg.fon.chatbot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/rest")
public class MessengerRestController {

    @RequestMapping(value = "/chatbot", method = RequestMethod.POST)
    public @ResponseBody Object receiveMessage(Object json){
        return ResponseEntity.status(HttpStatus.OK).body("Radi " + json.toString());
    }
}
