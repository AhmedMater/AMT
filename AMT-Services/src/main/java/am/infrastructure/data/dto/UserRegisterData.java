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
public class UserRegisterData implements Serializable{
    @NotNull(message = FIRST_NAME.REQUIRED)
    @NotEmpty(message = FIRST_NAME.EMPTY_STR)
    @Length(min = 1, max = 15, message = FIRST_NAME.LENGTH)
    @Pattern(regexp = RegExp.REAL_NAME, message = FIRST_NAME.INVALID)
    private String firstName;

    @NotNull(message = LAST_NAME.REQUIRED)
    @NotEmpty(message = LAST_NAME.EMPTY_STR)
    @Length(min = 1, max = 15, message = LAST_NAME.LENGTH)
    @Pattern(regexp = RegExp.REAL_NAME, message = LAST_NAME.INVALID)
    private String lastName;

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

    @NotNull(message = EMAIL.REQUIRED)
    @NotEmpty(message = EMAIL.EMPTY_STR)
    @Length(min = 10, max = 100, message = EMAIL.LENGTH)
    @Pattern(regexp = RegExp.EMAIL, message = EMAIL.INVALID)
    private String email;

    public UserRegisterData() {
    }
    public UserRegisterData(String firstName, String lastName, String username, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String fullName(){
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegisterData)) return false;

        UserRegisterData that = (UserRegisterData) o;

        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null) return false;
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null) return false;
        if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null) return false;
        if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null) return false;
        return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirstName() != null ? getFirstName().hashCode() : 0;
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRegisterData{" +
                "firstName = " + firstName +
                ", lastName = " + lastName +
                ", username = " + username +
                ", password = " + (password != null ? "Password" : "Null") +
                ", email = " + email +
                "}\n";
    }
}
