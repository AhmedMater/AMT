package am.infrastructure.data.hibernate.model.user;

import am.main.exception.BusinessException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahmed.motair on 10/28/2017.
 */
@Entity
@Cacheable(false)
@Table(name = "user_login_log")
public class UserLoginLog implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_login_log_id")
    private Integer userLoginLogID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @Basic
    @Column(name = "ip")
    private String ip;

    @Basic
    @Column(name = "login_date")
    private Date loginDate;

    @Basic
    @Column(name = "is_success")
    private Boolean isSuccess;

    @Basic
    @Column(name = "error_code")
    private String errorCode;

    @Basic
    @Column(name = "error_msg")
    private String errorMsg;

    public UserLoginLog() {
    }
    public UserLoginLog(Users user, String ip) {
        this.user = user;
        this.ip = ip;
        this.loginDate = new Date();
        this.isSuccess = true;
    }
    public UserLoginLog(UserIPDeActive userIPDeActive, BusinessException ex) {
        this.user = userIPDeActive.getUser();
        this.ip = userIPDeActive.getIp();
        this.loginDate = new Date();
        this.isSuccess = false;
        this.errorCode = ex.getErrorCode().toString();
        this.errorMsg = ex.getFormattedError();
    }
    public UserLoginLog(Users user, String ip, Date loginDate, Boolean isSuccess, String errorCode, String errorMsg) {
        this.user = user;
        this.ip = ip;
        this.loginDate = loginDate;
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getUserLoginLogID() {
        return userLoginLogID;
    }
    public void setUserLoginLogID(Integer userLoginLogID) {
        this.userLoginLogID = userLoginLogID;
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

    public Date getLoginDate() {
        return loginDate;
    }
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }
    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLoginLog)) return false;

        UserLoginLog that = (UserLoginLog) o;

        return getUserLoginLogID() != null ? getUserLoginLogID().equals(that.getUserLoginLogID()) : that.getUserLoginLogID() == null;
    }

    @Override
    public int hashCode() {
        return getUserLoginLogID() != null ? getUserLoginLogID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserLoginLog{" +
                "userLoginLogID = " + userLoginLogID +
                ", user = " + user +
                ", ip = " + ip +
                ", loginDate = " + loginDate +
                ", isSuccess = " + isSuccess +
                ", errorCode = " + errorCode +
                ", errorMsg = " + errorMsg +
                "}\n";
    }
}
