package am.infrastructure.data.dto.user;

import am.main.common.validation.RegExp;
import am.main.common.validation.groups.BlankValidation;
import am.main.common.validation.groups.InvalidValidation;
import am.main.common.validation.groups.LengthValidation;
import am.main.common.validation.groups.RequiredValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static am.shared.common.ValidationErrorMsg.PASSWORD;
import static am.shared.common.ValidationErrorMsg.USERNAME;

/**
 * Created by ahmed.motair on 10/26/2017.
 */
public class LoginData implements Serializable, Cloneable{
    @NotNull(message = USERNAME.REQUIRED, groups = RequiredValidation.class)
    @NotEmpty(message = USERNAME.EMPTY_STR, groups = BlankValidation.class)
    @Size(min = 5, max = 50, message = USERNAME.LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.USERNAME, message = USERNAME.INVALID, groups = InvalidValidation.class)
    private String username;

    @NotNull(message = PASSWORD.REQUIRED, groups = RequiredValidation.class)
    @NotEmpty(message = PASSWORD.EMPTY_STR, groups = BlankValidation.class)
    @Size(min = 5, max = 30, message = PASSWORD.LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.PASSWORD, message = PASSWORD.INVALID, groups = InvalidValidation.class)
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

    @Override
    public LoginData clone() throws CloneNotSupportedException {
        super.clone();
        LoginData clone = new LoginData();
        clone.setUsername(this.username);
        clone.setPassword(this.password);
        return clone;
    }
}
