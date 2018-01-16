package rs.ac.bg.fon.chatbot.db.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.repositories.ProfessorRepository;
import rs.ac.bg.fon.chatbot.db.domain.Professor;

@Service("professorService")
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    public void saveProfessor(Professor professor){
        professorRepository.save(professor);
    }

}
