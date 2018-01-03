package am.infrastructure.data.view.ui;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahmed.motair on 1/1/2018.
 */
public class UserListUI implements Serializable {
    private Integer userID;
    private String realName;
    private String role;
    private Date creationDate;

    public UserListUI() {
    }
    public UserListUI(Integer userID, String realName, String role, Date creationDate) {
        this.userID = userID;
        this.realName = realName;
        this.role = role;
        this.creationDate = new Date(creationDate.getTime());
    }

    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreationDate() {
        return new Date(this.creationDate.getTime());
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = new Date(creationDate.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserListUI)) return false;

        UserListUI that = (UserListUI) o;

        return getUserID() != null ? getUserID().equals(that.getUserID()) : that.getUserID() == null;
    }

    @Override
    public int hashCode() {
        return getUserID() != null ? getUserID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserListUI{" +
                "userID = " + userID +
                ", realName = " + realName +
                ", role = " + role +
                ", creationDate = " + creationDate +
                "}\n";
    }
}
