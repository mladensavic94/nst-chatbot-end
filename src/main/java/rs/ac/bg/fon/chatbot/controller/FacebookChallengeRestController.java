/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.chatbot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Mladen
 */
@Controller
@RequestMapping("/rest")
public class FacebookChallengeRestController {

    private final String TOKEN = "EAAZATKY8ZBibMBAJzzyx384nZBWRPAnIJy6u7RvEIFT36rmZBawFv8R5QjZB54V2FJ8suhzouRYtoDJyWtsvc1DgC4lZCFXkxY2o2XHHsVN2iiMrfrWafQjGaQ0VPnREZBH6cU5NxnKsPyCWqJJMq0y5iw6HHAHmaapZBHGryU3dgYjQfLAnONbv";

    @RequestMapping("/chatbot")
    public @ResponseBody Object validateFacebookApp(@RequestParam("hub.mode") String mode, @RequestParam("hub.verify_token") String verify_token,
                                                    @RequestParam("hub.challenge") String challenge){
        if(verify_token.equals(TOKEN))
        return ResponseEntity.status(HttpStatus.OK).body(challenge);
        return null;
    }
}
