package am.infrastructure.data.dto.course;

import am.infrastructure.data.hibernate.model.course.CoursePreRequisite;
import am.main.common.validation.RegExp;
import am.main.common.validation.groups.BlankValidation;
import am.main.common.validation.groups.InvalidValidation;
import am.main.common.validation.groups.LengthValidation;
import am.main.common.validation.groups.RequiredValidation;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

import static am.shared.common.ValidationErrorMsg.COURSE_PRE_REQUISITE;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CoursePRData implements Serializable{
    @NotNull(message = COURSE_PRE_REQUISITE.ORDER.REQUIRED, groups = RequiredValidation.class)
    @Positive(message = COURSE_PRE_REQUISITE.ORDER.INVALID, groups = InvalidValidation.class)
    @Min(value = 1, message = COURSE_PRE_REQUISITE.ORDER.MIN_VALUE, groups = InvalidValidation.class)
    private Integer num;

    @NotNull(message = COURSE_PRE_REQUISITE.NAME.REQUIRED, groups = RequiredValidation.class)
    @NotEmpty(message = COURSE_PRE_REQUISITE.NAME.EMPTY_STR, groups = BlankValidation.class)
    @Length(min = 5, max = 100, message = COURSE_PRE_REQUISITE.NAME.LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.CONTENT_NAME, message = COURSE_PRE_REQUISITE.NAME.INVALID, groups = InvalidValidation.class)
    private String name;

    @NotNull(message = COURSE_PRE_REQUISITE.TYPE.REQUIRED, groups = RequiredValidation.class)
    @NotEmpty(message = COURSE_PRE_REQUISITE.TYPE.EMPTY_STR, groups = BlankValidation.class)
    @Length(min = 2, max = 2, message = COURSE_PRE_REQUISITE.TYPE.LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP_CHAR, message = COURSE_PRE_REQUISITE.TYPE.INVALID, groups = InvalidValidation.class)
    private String type;

    @NotNull(message = COURSE_PRE_REQUISITE.URL.REQUIRED, groups = RequiredValidation.class)
    @NotEmpty(message = COURSE_PRE_REQUISITE.URL.EMPTY_STR, groups = BlankValidation.class)
    @Length(min = 5, max = 200, message = COURSE_PRE_REQUISITE.URL.LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.URL, message = COURSE_PRE_REQUISITE.URL.INVALID, groups = InvalidValidation.class)
    private String url;

    public CoursePRData() {
    }
    public CoursePRData(Integer num, String name, String type, String url) {
        this.num = num;
        this.name = name;
        this.type = type;
        this.url = url;
    }
    public CoursePRData(CoursePreRequisite preReq) {
        this.num = preReq.getNum();
        this.name = preReq.getName();
        this.type = preReq.getType().getDescription();
        this.url = preReq.getUrl();
    }

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoursePRData)) return false;

        CoursePRData that = (CoursePRData) o;

        if (getNum() != null ? !getNum().equals(that.getNum()) : that.getNum() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        return getUrl() != null ? getUrl().equals(that.getUrl()) : that.getUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getNum() != null ? getNum().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoursePRData{" +
                "num = " + num +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }

    @Override
    public CoursePRData clone() {
        CoursePRData clone = new CoursePRData();
        clone.setNum(this.num);
        clone.setName(this.name);
        clone.setType(this.type);
        clone.setUrl(this.url);
        return clone;
    }
}
