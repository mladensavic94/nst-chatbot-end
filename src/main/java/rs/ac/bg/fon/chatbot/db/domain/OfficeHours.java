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

    @Column(name = "idprof")
    private Integer idprof;

    @Column(name = "begin_time")
    private Date beginTime;

    @Column(name = "end_time")
    private Date endTime;

    public OfficeHours(Integer id, Integer idprof, Date beginTime, Date endTime) {
        this.id = id;
        this.idprof = idprof;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public OfficeHours() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdprof() {
        return idprof;
    }

    public void setIdprof(Integer idprof) {
        this.idprof = idprof;
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

