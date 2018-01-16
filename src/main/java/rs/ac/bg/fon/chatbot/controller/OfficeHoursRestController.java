package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.services.OfficeHoursService;

@Controller
@RequestMapping("/rest")
public class OfficeHoursRestController {


    @Autowired
    OfficeHoursService officeHoursService;


    @RequestMapping(value = "/officehours", method = RequestMethod.GET)
    public ResponseEntity<Object> getOfficeHoursForProfessor(@RequestBody Long id){
        try {
            String response = ParsingUtil.parseListToJson(officeHoursService.findAllByProfessorId(Math.toIntExact(id)));
            System.out.println(response);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
