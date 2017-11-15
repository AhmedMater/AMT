package am.infrastructure.data.dto.course;

import am.common.validation.RegExp;
import am.infrastructure.data.hibernate.model.course.CourseReference;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static am.common.ValidationErrorMsg.COURSE_REFERENCE;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseRefData implements Serializable {
    @NotNull(message = COURSE_REFERENCE.ORDER.REQUIRED)
    @Min(value = 1, message = COURSE_REFERENCE.ORDER.INVALID)
    private Integer num;

    @NotNull(message = COURSE_REFERENCE.NAME.REQUIRED)
    @NotEmpty(message = COURSE_REFERENCE.NAME.EMPTY_STR)
    @Length(min = 5, max = 100, message = COURSE_REFERENCE.NAME.LENGTH)
    @Pattern(regexp = RegExp.CONTENT_NAME, message = COURSE_REFERENCE.NAME.INVALID)
    private String name;

    @NotNull(message = COURSE_REFERENCE.TYPE.REQUIRED)
    @NotEmpty(message = COURSE_REFERENCE.TYPE.EMPTY_STR)
    @Length(min = 2, max = 2, message = COURSE_REFERENCE.TYPE.LENGTH)
    private String type;

    @NotNull(message = COURSE_REFERENCE.URL.REQUIRED)
    @NotEmpty(message = COURSE_REFERENCE.URL.EMPTY_STR)
    @Length(min = 5, max = 200, message = COURSE_REFERENCE.URL.LENGTH)
    @Pattern(regexp = RegExp.URL, message = COURSE_REFERENCE.URL.INVALID)
    private String url;

    public CourseRefData() {
    }
    public CourseRefData(Integer num, String name, String type, String url) {
        this.num = num;
        this.name = name;
        this.type = type;
        this.url = url;
    }
    public CourseRefData(CourseReference ref) {
        this.name = ref.getName();
        this.type = ref.getType().getDescription();
        this.url = ref.getUrl();
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
        if (!(o instanceof CourseRefData)) return false;

        CourseRefData that = (CourseRefData) o;

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
        return "CourseRefData{" +
                "num = " + num +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }
}
