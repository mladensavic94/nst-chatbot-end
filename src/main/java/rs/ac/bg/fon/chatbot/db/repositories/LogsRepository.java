package rs.ac.bg.fon.chatbot.db.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.ac.bg.fon.chatbot.db.domain.Logs;


public interface LogsRepository extends CrudRepository<Logs, String> {
}
