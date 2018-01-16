package rs.ac.bg.fon.chatbot.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;

public interface OfficeHoursRepository extends CrudRepository<OfficeHours, Integer> {

    @Query("select o from OfficeHours o where o.professor.id = ?1")
    Iterable<OfficeHours> findAllByProfessorId(Long id);

}
