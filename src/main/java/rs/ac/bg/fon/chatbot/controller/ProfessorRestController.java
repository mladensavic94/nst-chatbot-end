package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

@Controller
@RequestMapping("/rest")
public class ProfessorRestController {

    @Autowired
    ProfessorService professorService;

    @RequestMapping(value = "/professor", method = RequestMethod.GET)
    public ResponseEntity<Object> receiveMessage() {
        try {
            Professor professor = new Professor(null, "mladen@gmail.com", "msavic", "mladen", "savic");
            professorService.saveProfessor(professor);
            System.out.println(professor.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
