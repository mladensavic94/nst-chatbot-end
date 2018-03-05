package rs.ac.bg.fon.chatbot.db.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

public interface AppointmentsRepository extends CrudRepository<Appointment, Integer> {
}
