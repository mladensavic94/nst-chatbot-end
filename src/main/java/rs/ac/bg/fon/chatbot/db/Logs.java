package rs.ac.bg.fon.chatbot.db;

import org.springframework.data.jpa.repository.JpaRepository;

public class Logs{

    String log;

    public Logs(String log){
        this.log = log;
    }
}
