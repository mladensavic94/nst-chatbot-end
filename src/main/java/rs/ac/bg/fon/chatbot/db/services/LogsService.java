package rs.ac.bg.fon.chatbot.db.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.services.repositories.LogsRepository;
import rs.ac.bg.fon.chatbot.db.domain.Logs;

@Service
public class LogsService{

    private final
    LogsRepository logsRepository;

    @Autowired
    public LogsService(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    public void saveLog(Logs logs){
        logsRepository.save(logs);
    }

}
