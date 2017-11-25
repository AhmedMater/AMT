package am.infrastructure.data.view;

import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.data.hibernate.model.user.Users;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 11/24/2017.
 */
public class UserProfileData implements Serializable{
    private String fullName;
    private String email;
    private String role;

    private Boolean canEdit;
    private Boolean canUpgradeRole;

    private List<Role> roleList;

    public UserProfileData() {
    }
    public UserProfileData(Users user) {
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole().getDescription();
    }
    public UserProfileData(String fullName, String email, String role) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }
    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanUpgradeRole() {
        return canUpgradeRole;
    }
    public void setCanUpgradeRole(Boolean canUpgradeRole) {
        this.canUpgradeRole = canUpgradeRole;
    }

    public List<Role> getRoleList() {
        return roleList;
    }
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfileData)) return false;

        UserProfileData that = (UserProfileData) o;

        if (getFullName() != null ? !getFullName().equals(that.getFullName()) : that.getFullName() != null) return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getRole() != null ? !getRole().equals(that.getRole()) : that.getRole() != null) return false;
        if (getCanEdit() != null ? !getCanEdit().equals(that.getCanEdit()) : that.getCanEdit() != null) return false;
        if (getCanUpgradeRole() != null ? !getCanUpgradeRole().equals(that.getCanUpgradeRole()) : that.getCanUpgradeRole() != null) return false;
        return getRoleList() != null ? getRoleList().equals(that.getRoleList()) : that.getRoleList() == null;
    }

    @Override
    public int hashCode() {
        int result = getFullName() != null ? getFullName().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        result = 31 * result + (getCanEdit() != null ? getCanEdit().hashCode() : 0);
        result = 31 * result + (getCanUpgradeRole() != null ? getCanUpgradeRole().hashCode() : 0);
        result = 31 * result + (getRoleList() != null ? getRoleList().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
                "fullName = " + fullName +
                ", email = " + email +
                ", role = " + role +
                ", canEdit = " + canEdit +
                ", canUpgradeRole = " + canUpgradeRole +
                ", roleList = " + roleList +
                "}\n";
    }
}
