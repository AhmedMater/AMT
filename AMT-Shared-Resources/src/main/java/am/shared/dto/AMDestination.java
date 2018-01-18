package am.shared.dto;

import java.io.Serializable;

/**
 * Created by ahmed.motair on 1/15/2018.
 */
public class AMDestination implements Serializable{
    private String email;
    private String phone;
    private String userID;

    public AMDestination() {
    }
    public AMDestination(String email, String phone, String userID) {
        this.email = email;
        this.phone = phone;
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AMDestination)) return false;

        AMDestination that = (AMDestination) o;

        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null) return false;
        return getUserID() != null ? getUserID().equals(that.getUserID()) : that.getUserID() == null;
    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getUserID() != null ? getUserID().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AMDestination{" +
                "email = " + email +
                ", phone = " + phone +
                ", userID = " + userID +
                "}\n";
    }
}
