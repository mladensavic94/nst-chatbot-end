package rs.ac.bg.fon.chatbot.db.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.repositories.ProfessorRepository;

@Service
public class ProfessorService {

    private final
    ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void saveProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    public Iterable<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findByUsernameAndPassword(String email, String password) {
        return professorRepository.findByUsernameAndPassword(email, password);
    }

}
