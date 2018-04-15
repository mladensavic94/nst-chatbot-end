package rs.ac.bg.fon.chatbot.db.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "appointment", schema = "public")
public class Appointment implements Serializable{

    @Id
    @GeneratedValue(generator = "sidappointment")
    @SequenceGenerator(name = "sidappointment", sequenceName = "sidappointment")
    @Column(name = "idappointment")
    private Long id;

    @Column(name = "date")
    private Date dateAndTime;

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

    @ManyToOne
    @JoinColumn(name = "idofficehours")
    private OfficeHours officeHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Appointment(Date dateAndTime, String studentID, Integer length, Status status, String name, Professor professor) {
        this.dateAndTime = dateAndTime;
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

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void checkForStatusChange(){
        if(dateAndTime != null && studentID != null && professor != null){
            status = Status.FULL;
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", dateAndTime=" + dateAndTime +
                ", studentID='" + studentID + '\'' +
                ", length=" + length +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", professor=" + professor +
                ", officeHours=" + officeHours +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (dateAndTime != null ? !dateAndTime.equals(that.dateAndTime) : that.dateAndTime != null) return false;
        if (studentID != null ? !studentID.equals(that.studentID) : that.studentID != null) return false;
        if (length != null ? !length.equals(that.length) : that.length != null) return false;
        if (status != that.status) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (professor != null ? !professor.equals(that.professor) : that.professor != null) return false;
        return officeHours != null ? officeHours.equals(that.officeHours) : that.officeHours == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateAndTime != null ? dateAndTime.hashCode() : 0);
        result = 31 * result + (studentID != null ? studentID.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (professor != null ? professor.hashCode() : 0);
        result = 31 * result + (officeHours != null ? officeHours.hashCode() : 0);
        return result;
    }

    public OfficeHours getOfficeHours() {

        return officeHours;
    }

    public void setOfficeHours(OfficeHours officeHours) {
        this.officeHours = officeHours;
    }
}
