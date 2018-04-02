package rs.ac.bg.fon.chatbot.db.services.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;

import java.util.Date;
import java.util.List;

public interface OfficeHoursRepository extends CrudRepository<OfficeHours, Long> {

    @Query("select o from OfficeHours o, Professor p where o.professor = p and p.email = ?1 order by o.beginTime desc")
    List<OfficeHours> findAllByProfessorEmail(String email);

    @Query("select o from OfficeHours o, Professor p where o.professor = p and p.email = ?1 and o.beginTime > ?2")
    List<OfficeHours> findAllByProfessorEmail(String email, Date date);
}
