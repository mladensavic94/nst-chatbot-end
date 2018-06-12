package rs.ac.bg.fon.chatbot.db.services.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.chatbot.db.domain.Professor;

public interface ProfessorRepository extends CrudRepository<Professor, Long>{

    @Query("select p from Professor p where p.email=?1 and p.password=?2")
    Professor findByUsernameAndPassword(String email, String password);

    @Query("select p from Professor p where p.email=?1")
    Professor findByUsername(String s);
}
