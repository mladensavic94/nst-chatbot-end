package rs.ac.bg.fon.chatbot.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

public interface AppointmentsRepository extends CrudRepository<Appointment, Integer> {

    @Query("select a from Appointment a where a.officeHours.professor.email = ?1 order by a.officeHours.beginTime desc")
    Iterable<Appointment> findAllByEmail(String email);
}
