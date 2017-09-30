package am.infrastructure.data.hibernate.model.user;

import am.infrastructure.data.enums.Roles;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
@Entity
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "LookupRegion")
@Table(name = "role")
public class Role implements Serializable{
    @Id
    @Column(name = "role")
    private String role;

    @Basic
    @Column(name = "description")
    private String description;

    public Role() {
    }
    public Role(String role) {
        this(role, null);
    }
    public Role(Roles role) {
        this(role.role(), role.description());
    }
    public Role(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role1 = (Role) o;

        return getRole() != null ? getRole().equals(role1.getRole()) : role1.getRole() == null;
    }

    @Override
    public int hashCode() {
        return getRole() != null ? getRole().hashCode() : 0;
    }

    @Override
    public String toString() {
        return role;
    }
}
