package am.infrastructure.data.hibernate.model.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahmed.motair on 10/28/2017.
 */
@Entity
@Cacheable(false)
@Table(name = "user_ip_deactive")
public class UserIPDeActive implements Serializable{
    public static final String USER_NAME = "user.username";
    public static final String PASSWORD = "user.password";
    public static final String IP = "ip";
    public static final String IS_ACTIVE = "isActive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ip_deactive_id")
    private Integer userIPDeActiveID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @Basic
    @Column(name = "ip")
    private String ip;

    @Basic
    @Column(name = "trail_num")
    private Integer trailNum;

    @Basic
    @Column(name = "last_trail_date")
    private Date lastTrailDate;

    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    public UserIPDeActive() {
    }
    public UserIPDeActive(Users user, String ip, Integer trailNum, Date lastTrailDate, Boolean isActive) {
        this.user = user;
        this.ip = ip;
        this.trailNum = trailNum;
        this.lastTrailDate = lastTrailDate;
        this.isActive = isActive;
    }
    public UserIPDeActive(Users user, String ip) {
        this.user = user;
        this.ip = ip;
        this.trailNum = 1;
        this.lastTrailDate = new Date();
        this.isActive = true;
    }

    public Integer getUserIPDeActiveID() {
        return userIPDeActiveID;
    }
    public void setUserIPDeActiveID(Integer userIPDeActiveID) {
        this.userIPDeActiveID = userIPDeActiveID;
    }

    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTrailNum() {
        return trailNum;
    }
    public void setTrailNum(Integer trailNum) {
        this.trailNum = trailNum;
    }

    public Date getLastTrailDate() {
        return lastTrailDate;
    }
    public void setLastTrailDate(Date lastTrailDate) {
        this.lastTrailDate = lastTrailDate;
    }

    public Boolean getActive() {
        return isActive;
    }
    public void setActive(Boolean active) {
        isActive = active;
    }

    public void activeIP(){
        this.trailNum = 1;
        this.lastTrailDate = new Date();
        this.isActive = true;
    }
    public void deActiveIP(){
        this.trailNum++;
        this.lastTrailDate = new Date();
        this.isActive = false;
    }
    public void incTrail(){
        this.trailNum++;
        this.lastTrailDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserIPDeActive)) return false;

        UserIPDeActive that = (UserIPDeActive) o;

        return getUserIPDeActiveID() != null ? getUserIPDeActiveID().equals(that.getUserIPDeActiveID()) : that.getUserIPDeActiveID() == null;
    }

    @Override
    public int hashCode() {
        return getUserIPDeActiveID() != null ? getUserIPDeActiveID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserIPDeActive{" +
                "userIPDeActiveID = " + userIPDeActiveID +
                ", user = " + user +
                ", ip = " + ip +
                ", trailNum = " + trailNum +
                ", lastTrailDate = " + lastTrailDate +
                ", isActive = " + isActive +
                "}\n";
    }
}
