/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Mladen
 */
@Controller
public class IndexController {
    
    @RequestMapping("/")
    public ModelAndView start(){
        return new ModelAndView("index.jsp");
    }
    
}
