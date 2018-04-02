package rs.ac.bg.fon.chatbot.db.services.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

public interface AppointmentsRepository extends CrudRepository<Appointment, Long> {

    @Query("select a from Appointment a where a.officeHours.professor.email = ?1 order by a.officeHours.beginTime desc")
    Iterable<Appointment> findAllByEmail(String email);

    @Query("select a from Appointment a where a.studentID = ?1 and a.status = 'OPEN'")
    Appointment findByStudentID(String studentid);

}