package rs.ac.bg.fon.chatbot.db.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.repositories.ProfessorRepository;

@Service("professorService")
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    public void saveProfessor(Professor professor){
        professorRepository.save(professor);
    }

    public Iterable<Professor> findAll(){
        return professorRepository.findAll();
    }

}
