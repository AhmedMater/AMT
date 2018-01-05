package am.infrastructure.data.dto.filters;

import am.main.api.validation.custom.annotation.NullAndNotBlank;
import am.main.api.validation.groups.*;
import am.main.common.RegExp;
import am.main.data.dto.SortingInfo;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed.motair on 1/1/2018.
 */
public class UserListFilter implements Serializable {
    public static final Map<String, String> FIELDS = Collections.unmodifiableMap(
            new HashMap<String, String>(){{
                put("realName", "Real Name");
                put("role", "Role");
                put("creationDateFrom", "Creation Date From");
                put("creationDateTo", "Creation Date To");
                put("pageNum", "Page Number");
                put("sorting", "Sorting Info");
            }}
    );

    @NullAndNotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    @Length(max = 30, message = FormValidation.MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.FULL_NAME, message = FormValidation.REGEX, groups = InvalidValidation.class)
    private String realName;

    @NullAndNotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.EQ_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    private String role;

    private Date creationDateFrom;
    private Date creationDateTo;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @PositiveOrZero(message = FormValidation.POSITIVE_NUM_AND_ZERO, groups = InvalidValidation.class)
    private Integer pageNum;

    private SortingInfo sorting;

    public UserListFilter() {
    }
    public UserListFilter(String realName, String role, Date creationDateFrom, Date creationDateTo, Integer pageNum, SortingInfo sorting) {
        this.realName = realName;
        this.role = role;
        this.creationDateFrom = creationDateFrom;
        this.creationDateTo = creationDateTo;
        this.pageNum = pageNum;
        this.sorting = sorting;
    }

    public static Map<String, String> getFIELDS() {
        return FIELDS;
    }

    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreationDateFrom() {
        return creationDateFrom;
    }
    public void setCreationDateFrom(Date creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    public Date getCreationDateTo() {
        return creationDateTo;
    }
    public void setCreationDateTo(Date creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public SortingInfo getSorting() {
        return sorting;
    }
    public void setSorting(SortingInfo sorting) {
        this.sorting = sorting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserListFilter)) return false;

        UserListFilter that = (UserListFilter) o;

        if (getRealName() != null ? !getRealName().equals(that.getRealName()) : that.getRealName() != null)
            return false;
        if (getRole() != null ? !getRole().equals(that.getRole()) : that.getRole() != null) return false;
        if (getCreationDateFrom() != null ? !getCreationDateFrom().equals(that.getCreationDateFrom()) : that.getCreationDateFrom() != null)
            return false;
        if (getCreationDateTo() != null ? !getCreationDateTo().equals(that.getCreationDateTo()) : that.getCreationDateTo() != null)
            return false;
        if (getPageNum() != null ? !getPageNum().equals(that.getPageNum()) : that.getPageNum() != null) return false;
        return getSorting() != null ? getSorting().equals(that.getSorting()) : that.getSorting() == null;
    }

    @Override
    public int hashCode() {
        int result = getRealName() != null ? getRealName().hashCode() : 0;
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        result = 31 * result + (getCreationDateFrom() != null ? getCreationDateFrom().hashCode() : 0);
        result = 31 * result + (getCreationDateTo() != null ? getCreationDateTo().hashCode() : 0);
        result = 31 * result + (getPageNum() != null ? getPageNum().hashCode() : 0);
        result = 31 * result + (getSorting() != null ? getSorting().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserListFilter{" +
                "realName = " + realName +
                ", role = " + role +
                ", creationDateFrom = " + creationDateFrom +
                ", creationDateTo = " + creationDateTo +
                ", pageNum = " + pageNum +
                ", sorting = " + sorting +
                "}\n";
    }
}
