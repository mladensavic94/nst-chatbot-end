package rs.ac.bg.fon.chatbot.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

public interface LogsService extends JpaRepository<Logs, String> {

}
