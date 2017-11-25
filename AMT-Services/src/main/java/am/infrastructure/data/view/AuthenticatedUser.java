package am.infrastructure.data.view;

import am.infrastructure.data.hibernate.model.user.Users;

import java.io.Serializable;

/**
 * Created by ahmed.motair on 9/30/2017.
 */
public class AuthenticatedUser implements Serializable{
    private String username;
    private String fullName;
    private String token;
    private String role;
    private Integer userID;

    public AuthenticatedUser() {
    }
    public AuthenticatedUser(Users user, String token) {
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.userID = user.getUserID();
        this.token = token;
        this.role = user.getRole().getRole();
    }

    public AuthenticatedUser(String fullName, String token, String role) {
        this.fullName = fullName;
        this.token = token;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticatedUser)) return false;

        AuthenticatedUser that = (AuthenticatedUser) o;

        if (getFullName() != null ? !getFullName().equals(that.getFullName()) : that.getFullName() != null)
            return false;
        if (getToken() != null ? !getToken().equals(that.getToken()) : that.getToken() != null) return false;
        return getRole() != null ? getRole().equals(that.getRole()) : that.getRole() == null;
    }

    @Override
    public int hashCode() {
        int result = getFullName() != null ? getFullName().hashCode() : 0;
        result = 31 * result + (getToken() != null ? getToken().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthenticatedUser{" +
                "fullName = " + fullName +
                ", username = " + username +
                ", token = " + token +
                ", role = " + role +
                ", userID = " + userID +
                "}\n";
    }
}
