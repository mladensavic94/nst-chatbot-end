package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.services.OfficeHoursService;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

@Controller
@RequestMapping("/rest")
public class OfficeHoursRestController {


    private final OfficeHoursService officeHoursService;
    private final ProfessorService professorService;

    @Autowired
    public OfficeHoursRestController(OfficeHoursService officeHoursService, ProfessorService professorService) {
        this.officeHoursService = officeHoursService;
        this.professorService = professorService;
    }

    @RequestMapping(value = "/officehours", method = RequestMethod.GET)
    public ResponseEntity<Object> getOfficeHoursForProfessor(@RequestParam String email) {
        try {
            String response = ParsingUtil.parseListToJson(officeHoursService.findAllByProfessorId(email));
            System.out.println(response);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/officehours", method = RequestMethod.POST)
    public ResponseEntity<Object> postOfficeHours(@RequestBody String json) {
        try {
            OfficeHours officeHours = ParsingUtil.parseJsonToDomainObject(json, OfficeHours.class);
            professorService.saveProfessor(officeHours.getProfessor());
            officeHoursService.saveOfficeHours(officeHours);
            System.out.println(officeHours);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
