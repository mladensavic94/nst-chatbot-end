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
    private Integer id;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "idofficehours")
    private OfficeHours officeHours;

    @Column(name = "studentID")
    private String studentID;

    @Column(name = "length")
    private Integer length;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "studentnName")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OfficeHours getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(OfficeHours officeHours) {
        this.officeHours = officeHours;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
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

    public Appointment(OfficeHours officeHours, String studentID, Integer length, Status status, String name) {
        this.officeHours = officeHours;
        this.studentID = studentID;
        this.length = length;
        this.status = status;
        this.name = name;
    }

    public Appointment() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", officeHours=" + officeHours +
                ", studentID='" + studentID + '\'' +
                ", length=" + length +
                ", status=" + status +
                '}';
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
