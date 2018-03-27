package rs.ac.bg.fon.chatbot.db.domain;

import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "office_hours", schema = "public")
public class OfficeHours {

    @Id
    @GeneratedValue(generator = "sidofficehours")
    @SequenceGenerator(name = "sidofficehours", sequenceName = "sidofficehours")
    @Column(name = "idofficehours")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idprofessor")
    private Professor professor;

    @Column(name = "begin_time")
    private Date beginTime;

    @Column(name = "end_time")
    private Date endTime;

    @OneToMany(mappedBy = "officeHours", fetch = FetchType.LAZY)
    private transient List<Appointment> listOfAppointments;

    public OfficeHours(Professor professor, Date beginTime, Date endTime, List<Appointment> listOfAppointments) {
        this.professor = professor;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.listOfAppointments = listOfAppointments;
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

    public List<Appointment> getListOfAppointments() {
        return listOfAppointments;
    }

    public void setListOfAppointments(List<Appointment> listOfAppointments) {
        this.listOfAppointments = listOfAppointments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

