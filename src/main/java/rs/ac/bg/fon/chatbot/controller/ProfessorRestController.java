package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

@Controller
@RequestMapping("/rest")
public class ProfessorRestController {

    private final
    ProfessorService professorService;

    @Autowired
    public ProfessorRestController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @RequestMapping(value = "/professors", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllProfessors() {
        try {
            String response = ParsingUtil.parseListToJson(professorService.findAll());
            System.out.println(response);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/professor", method = RequestMethod.GET)
    public ResponseEntity<Object> getProfessor(@RequestParam String email) {
        try {
            String response = ParsingUtil.parseDomainObjectToJson(professorService.findByEmail(email));
            System.out.println(response);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @RequestMapping(value = "/professor/save", method = RequestMethod.POST)
    public ResponseEntity<Object> saveProfessor(@RequestBody String json){
        try {
            Professor professor = ParsingUtil.parseJsonToDomainObject(json, Professor.class);
            professorService.saveProfessor(professor);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
