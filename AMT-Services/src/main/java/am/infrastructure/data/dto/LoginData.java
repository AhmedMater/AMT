package am.infrastructure.data.dto;

import am.common.validation.RegExp;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static am.common.ValidationErrorMsg.*;

/**
 * Created by ahmed.motair on 10/26/2017.
 */
public class LoginData implements Serializable{
    @NotNull(message = USERNAME.REQUIRED)
    @NotEmpty(message = USERNAME.EMPTY_STR)
    @Length(min = 5, max = 50, message = USERNAME.LENGTH)
    @Pattern(regexp = RegExp.USERNAME, message = USERNAME.INVALID)
    private String username;

    @NotNull(message = PASSWORD.REQUIRED)
    @NotEmpty(message = PASSWORD.EMPTY_STR)
    @Length(min = 5, max = 30, message = PASSWORD.LENGTH)
    @Pattern(regexp = RegExp.PASSWORD, message = PASSWORD.INVALID)
    private String password;

    public LoginData() {
    }
    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginData)) return false;

        LoginData loginData = (LoginData) o;

        if (getUsername() != null ? !getUsername().equals(loginData.getUsername()) : loginData.getUsername() != null) return false;
        return getPassword() != null ? getPassword().equals(loginData.getPassword()) : loginData.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "username = " + username +
                ", password = " + (password != null ? "Password" : "Null") +
                "}\n";
    }
}
