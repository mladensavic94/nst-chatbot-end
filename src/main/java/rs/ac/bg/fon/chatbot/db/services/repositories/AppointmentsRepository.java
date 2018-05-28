package rs.ac.bg.fon.chatbot.db.services.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

import java.util.List;

public interface AppointmentsRepository extends CrudRepository<Appointment, Long> {

    @Query("select a from Appointment a where a.professor.email = ?1 order by a.dateAndTime desc")
    List<Appointment> findAllByEmail(String email);

    @Query("select a from Appointment a where a.studentID = ?1 and a.status = 'OPEN'")
    Appointment findByStudentID(String studentid);

    @Transactional
    @Modifying
    @Query("delete from Appointment a where a.studentID = ?1 and a.status= 'OPEN'")
    void deleteByStudentID(String s);
}
