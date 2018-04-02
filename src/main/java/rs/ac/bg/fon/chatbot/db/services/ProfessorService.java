package rs.ac.bg.fon.chatbot.db.services;


import org.apache.commons.text.similarity.JaroWinklerDistance;
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

    public Professor findProfessorUsingStringDistance(String professorString) throws Exception {
        double threshold = 0.95;
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        Professor result = null;
        double max = Double.MIN_VALUE;
        for (Professor professor : findAll()) {
            String[] profInfo = professorString.split(" ");
            double name2name = jaroWinklerDistance.apply(professor.getFirstName(), profInfo[0]);
            double name2lastname = jaroWinklerDistance.apply(professor.getFirstName(), profInfo[1]);
            double lastname2name = jaroWinklerDistance.apply(professor.getLastName(), profInfo[0]);
            double lastname2lastname = jaroWinklerDistance.apply(professor.getLastName(), profInfo[1]);
            double maxDistIme = Math.max(name2name, name2lastname);
            double maxDistPrezime = Math.max(lastname2name, lastname2lastname);
            if (max < (maxDistIme + maxDistPrezime) / 2) {
                max = (maxDistIme + maxDistPrezime) / 2;
                result = professor;
            }
        }
        if (max > threshold) {
            return result;
        } else {
            throw new Exception("UNKOWN");
        }
    }

}
