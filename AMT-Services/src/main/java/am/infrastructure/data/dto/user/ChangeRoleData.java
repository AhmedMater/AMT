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
 * Created by ahmed.motair on 12/9/2017.
 */
public class ChangeRoleData implements Serializable{
    public static final Map<String, String> FIELDS = Collections.unmodifiableMap(
        new HashMap<String, String>(){{ put("newRole", "New Role"); }}
    );

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.EQ_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotEmpty(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String newRole;

    public ChangeRoleData() {
    }
    public ChangeRoleData(String newRole) {
        this.newRole = newRole;
    }

    public String getNewRole() {
        return newRole;
    }
    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }

    public static Map<String, String> getFIELDS() {
        return FIELDS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeRoleData)) return false;

        ChangeRoleData that = (ChangeRoleData) o;

        return getNewRole() != null ? getNewRole().equals(that.getNewRole()) : that.getNewRole() == null;
    }

    @Override
    public int hashCode() {
        return getNewRole() != null ? getNewRole().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChangeRoleData{" +
                "newRole = " + newRole +
                "}\n";
    }
}
