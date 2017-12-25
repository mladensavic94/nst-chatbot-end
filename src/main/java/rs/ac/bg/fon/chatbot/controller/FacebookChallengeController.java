/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.chatbot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Mladen
 */
@Controller
@RequestMapping("/rest")
public class FacebookChallengeController {
    
    private final String TOKEN = "Jedi govna majmune";
    
    @RequestMapping("/validate")
    public @ResponseBody Object validateFacebookApp(){
        return ResponseEntity.status(HttpStatus.OK).body(TOKEN);
    }
}
