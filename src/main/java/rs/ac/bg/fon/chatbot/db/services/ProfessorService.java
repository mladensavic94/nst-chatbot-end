package rs.ac.bg.fon.chatbot.db.services;


import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.repositories.ProfessorRepository;

import java.util.ArrayList;

@Service
public class ProfessorService implements UserDetailsService {

    private final
    ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void saveProfessor(Professor professor) {
        Professor pom = professorRepository.findOne(professor.getIdprofessor());
        if (pom != null) {
            pom.setEmail(professor.getEmail());
            pom.setFirstName(professor.getFirstName());
            pom.setLastName(professor.getLastName());
            pom.setPassword(professor.getPassword());
            professor.getListOfOfficeHours().forEach(officeHours -> {
                System.out.println(officeHours);
                if(officeHours.getId() == 0){
                    pom.getListOfOfficeHours().add(officeHours);
                }
            });
            professorRepository.save(pom);
            return;
        }
        professorRepository.save(professor);
    }

    public Iterable<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findByEmail(String email) {
        return professorRepository.findByUsername(email);
    }

    public Professor findProfessorUsingStringDistance(String professorString) throws Exception {
        double threshold = 0.95;
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        Professor result = null;
        double max = Double.MIN_VALUE;
        for (Professor professor : findAll()) {
            String[] profInfo = professorString.split(" ");
            double name2name = jaroWinklerDistance.apply(professor.getFirstName().toLowerCase(), profInfo[0].toLowerCase());
            double name2lastname = jaroWinklerDistance.apply(professor.getFirstName().toLowerCase(), profInfo[1].toLowerCase());
            double lastname2name = jaroWinklerDistance.apply(professor.getLastName().toLowerCase(), profInfo[0].toLowerCase());
            double lastname2lastname = jaroWinklerDistance.apply(professor.getLastName().toLowerCase(), profInfo[1].toLowerCase());
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
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Professor professor = professorRepository.findByUsername(s);
        if (professor != null)
            return new User(professor.getEmail(), professor.getPassword(), new ArrayList<>());
        throw new UsernameNotFoundException(professor.getEmail());
    }
}
