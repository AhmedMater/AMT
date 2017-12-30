package am.infrastructure.data.dto.user;

import am.main.api.validation.groups.*;
import am.main.common.RegExp;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed.motair on 10/26/2017.
 */
public class LoginData implements Serializable, Cloneable{
    public static final Map<String, String> FIELDS = Collections.unmodifiableMap(
        new HashMap<String, String>(){{
            put("username", "Username");
            put("password", "Password");
        }}
    );

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 5, max = 50, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.USERNAME, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotEmpty(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String username;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 5, max = 30, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.PASSWORD, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotEmpty(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
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

    public static Map<String, String> getFIELDS() {
        return FIELDS;
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
