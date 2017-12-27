package rs.ac.bg.fon.chatbot.db.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Logs{

    @Id
    String log;

    public Logs(String log){
        this.log = log;
    }
}
