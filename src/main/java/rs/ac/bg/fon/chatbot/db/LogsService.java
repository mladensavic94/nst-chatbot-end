package rs.ac.bg.fon.chatbot.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Logs;

@Service(value = "logsService")
public class LogsService{

    @Autowired
    LogsRepository logsRepository;

    public void saveLog(Logs logs){
        logsRepository.save(logs);
    }

}
