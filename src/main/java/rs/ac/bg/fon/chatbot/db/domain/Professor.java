package rs.ac.bg.fon.chatbot.db.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "professor", schema = "public")
public class Professor implements Serializable {

    @Id
    @GeneratedValue(generator = "sidprofessor")
    @SequenceGenerator(name = "sidprofessor", sequenceName = "sidprofessor")
    @Column(name = "idprofessor")
    private Long idprofessor;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "professor", fetch = FetchType.EAGER)
    private transient List<OfficeHours> listOfOfficeHours;


    public Professor() {
    }

    public Professor(String email, String password, String firstName, String lastName, List<OfficeHours> listOfOfficeHours) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.listOfOfficeHours = listOfOfficeHours;
    }

    public List<OfficeHours> getListOfOfficeHours() {
        return listOfOfficeHours;
    }

    public void setListOfOfficeHours(List<OfficeHours> listOfOfficeHours) {
        this.listOfOfficeHours = listOfOfficeHours;
    }

    public Long getIdprofessor() {
        return idprofessor;
    }

    public void setIdprofessor(Long idprofessor) {
        this.idprofessor = idprofessor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        return Objects.equals(this.idprofessor, ((Professor) o).idprofessor);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "idprofessor=" + idprofessor +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
