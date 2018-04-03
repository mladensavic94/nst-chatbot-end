package rs.ac.bg.fon.chatbot.db.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "appointment", schema = "public")
public class Appointment implements Serializable{

    @Id
    @GeneratedValue(generator = "sidappointment")
    @SequenceGenerator(name = "sidappointment", sequenceName = "sidappointment")
    @Column(name = "idappointment")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idofficehours")
    private OfficeHours officeHours;

    @Column(name = "studentid")
    private String studentID;

    @Column(name = "length")
    private Integer length;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "studentname")
    private String name;

    @OneToOne
    @JoinColumn(name = "idprofessor")
    private Professor professor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OfficeHours getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(OfficeHours officeHours) {
        this.officeHours = officeHours;
        checkForStatusChange();
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
        checkForStatusChange();
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Status getStatus() {
        return status;
    }

    public Appointment(OfficeHours officeHours, String studentID, Integer length, Status status, String name, Professor professor) {
        this.officeHours = officeHours;
        this.studentID = studentID;
        this.length = length;
        this.status = status;
        this.name = name;
        this.professor = professor;
    }

    public Professor getProfessor() {

        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Appointment() {
        this.status = Status.OPEN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        checkForStatusChange();
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", officeHours=" + officeHours +
                ", studentID='" + studentID + '\'' +
                ", length=" + length +
                ", status=" + status +
                ", name=" + name +
                '}';
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void checkForStatusChange(){
        if(officeHours != null && studentID != null && professor != null){
            status = Status.FULL;
        }
    }
}
