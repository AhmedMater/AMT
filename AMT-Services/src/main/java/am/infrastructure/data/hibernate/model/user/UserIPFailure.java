package am.infrastructure.data.hibernate.model.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahmed.motair on 10/28/2017.
 */
@Entity
@Cacheable(false)
@Table(name = "user_ip_failure")
public class UserIPFailure implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ip_failure_id")
    private Integer userIPFailureID;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "ip")
    private String ip;

    @Basic
    @Column(name = "login_date")
    private Date loginDate;

    public UserIPFailure() {
    }
    public UserIPFailure(String username, String ip) {
        this.username = username;
        this.ip = ip;
        this.loginDate = new Date();
    }
    public UserIPFailure(String username, String ip, Date loginDate) {
        this.username = username;
        this.ip = ip;
        this.loginDate = loginDate;
    }

    public Integer getUserIPFailureID() {
        return userIPFailureID;
    }
    public void setUserIPFailureID(Integer userIPFailureID) {
        this.userIPFailureID = userIPFailureID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginDate() {
        return loginDate;
    }
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserIPFailure)) return false;

        UserIPFailure that = (UserIPFailure) o;

        return getUserIPFailureID() != null ? getUserIPFailureID().equals(that.getUserIPFailureID()) : that.getUserIPFailureID() == null;
    }

    @Override
    public int hashCode() {
        return getUserIPFailureID() != null ? getUserIPFailureID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserIPFailure{" +
                "userIPFailureID = " + userIPFailureID +
                ", username = " + username +
                ", ip = " + ip +
                ", loginDate = " + loginDate +
                "}\n";
    }
}
