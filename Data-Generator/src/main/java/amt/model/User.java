package amt.model;

import amt.generic.Gen;

import java.io.Serializable;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by ahmed.motair on 12/10/2017.
 */
public class User implements Serializable{
    private Integer userID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String role;
    private Date creationDate;

    public User() {
    }
    public User(String firstName, String lastName, String username, String password, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        if(firstName.length()>15)
            this.firstName = firstName.substring(0,15);
        else
            this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        if(lastName.length()>15)
            this.lastName = lastName.substring(0,15);
        else
            this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        if(username.length()>50)
            this.username = username.substring(0,50);
        else
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
        if(email.length()>60)
            this.email = email.substring(0,60);
        else
            this.email = email;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getFirstName() != null ? !getFirstName().equals(user.getFirstName()) : user.getFirstName() != null) return false;
        if (getLastName() != null ? !getLastName().equals(user.getLastName()) : user.getLastName() != null) return false;
        if (getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null) return false;
        if (getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null) return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        return getRole() != null ? getRole().equals(user.getRole()) : user.getRole() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirstName() != null ? getFirstName().hashCode() : 0;
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName = " + firstName +
                ", lastName = " + lastName +
                ", username = " + username +
                ", password = " + password +
                ", email = " + email +
                ", role = " + role +
                "}\n";
    }

    private static String INSERT_STATEMENT = "INSERT INTO users (user_id, first_name, last_name, user_name, " +
            "password, email, role, creation_date) VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', " +
            "''{6}'', ''{7}'');";

    public static String passwordHash(String password) throws Exception{
        MessageDigest md;

        md = MessageDigest.getInstance("MD5");

        md.update(password.getBytes());
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }

    public String generateSQL()throws Exception{
        return MessageFormat.format(INSERT_STATEMENT, userID.toString(), Gen.escapeSingleQuote(firstName),
                Gen.escapeSingleQuote(lastName), Gen.escapeSingleQuote(username),
                passwordHash(password), Gen.escapeSingleQuote(email), role, Gen.formatDate(creationDate));
    }

}
