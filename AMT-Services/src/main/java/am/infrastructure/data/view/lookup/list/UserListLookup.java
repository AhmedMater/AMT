package am.infrastructure.data.view.lookup.list;

import am.infrastructure.data.hibernate.model.lookup.Role;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 1/1/2018.
 */
public class UserListLookup implements Serializable{
    private List<Role> roles;

    public UserListLookup() {
    }
    public UserListLookup(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserListLookup)) return false;

        UserListLookup that = (UserListLookup) o;

        return getRoles() != null ? getRoles().equals(that.getRoles()) : that.getRoles() == null;
    }

    @Override
    public int hashCode() {
        return getRoles() != null ? getRoles().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserListLookup{" +
                "roles = " + roles +
                "}\n";
    }
}
