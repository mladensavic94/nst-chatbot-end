package rs.ac.bg.fon.chatbot.db.domain;

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
    @RecursiveJson
    private Professor professor;

    @Column(name = "begin_time")
    private Date beginTime;

    @Column(name = "end_time")
    private Date endTime;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "officeHours")
    private List<Appointment> appointments;

    public OfficeHours(Professor professor, Date beginTime, Date endTime, List<Appointment> appointments) {
        this.professor = professor;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.appointments = appointments;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
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

