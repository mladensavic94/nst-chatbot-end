package rs.ac.bg.fon.chatbot.db.domain;

import org.hibernate.annotations.GeneratorType;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@Entity
@Table(name = "logs", schema = "public")
public class Logs{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "\"IDLog\"")
    private int id;
    private String log;

    public Logs(String log){
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
