package am.infrastructure.data.hibernate.model.user;

import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.generic.ConfigParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
@Entity
@Cacheable(false)
@Table(name = "users")
public class Users {
    public static final String USER_ID = "userID";
    public static final String USER_NAME = "username";
    public static final String ROLE = "role." + Role.ROLE;
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CREATION_DATE = "creationDate";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userID;

    @Basic
    @Column(name = "user_name")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "role")
    private Role role;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "creation_date")
    private Date creationDate;

    public Users() {
    }
    public Users(Integer userID) {
        this.userID = userID;
    }
    public Users(Integer userID, String username, String password, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
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
    public String getFullName() {
        return firstName + " " + lastName;
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
        if (!(o instanceof Users)) return false;

        Users users = (Users) o;

        return getUserID() != null ? getUserID().equals(users.getUserID()) : users.getUserID() == null;
    }

    @Override
    public int hashCode() {
        return getUserID() != null ? getUserID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userID = " + userID +
                ", firstName = " + firstName +
                ", lastName = " + lastName +
                ", username = " + username +
                ", email = " + email +
                ", role = " + role +
                ", creationDate = " + creationDate +
                "}\n";
    }
}
