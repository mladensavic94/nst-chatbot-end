package rs.ac.bg.fon.chatbot.db.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "office_hours", schema = "public")
public class OfficeHours {

    @Id
    @GeneratedValue(generator = "sidofficehours")
    @SequenceGenerator(name = "sidofficehours", sequenceName = "sidofficehours")
    @Column(name = "idofficehours")
    private Integer id;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "idprofessor")
    private Professor professor;

    @Column(name = "begin_time")
    private Date beginTime;

    @Column(name = "end_time")
    private Date endTime;

    public OfficeHours(Integer id, Professor professor, Date beginTime, Date endTime) {
        this.id = id;
        this.professor = professor;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public OfficeHours() {
    }

    @Override
    public String toString() {
        return "OfficeHours{" +
                "id=" + id +
                ", professor=" + professor +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

