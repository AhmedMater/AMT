package am.infrastructure.data.dto.course;

import am.infrastructure.data.hibernate.model.course.CourseReference;
import am.main.api.validation.groups.*;
import am.main.common.RegExp;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseRefData implements Serializable {
    public static final Map<String, String> FIELDS = Collections.unmodifiableMap(
        new HashMap<String, String>(){{
            put("num", "Num");
            put("name", "Name");
            put("type", "Type");
            put("url", "URL");
        }}
    );

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Positive(message = FormValidation.POSITIVE_NUM, groups = InvalidValidation.class)
    @Min(value = 1, message = FormValidation.MIN_VALUE, groups = InvalidValidation.class)
    private Integer num;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 5, max = 100, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.CONTENT_NAME, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String name;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String type;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 5, max = 200, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.URL, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
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
        this.num = ref.getNum();
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

    public static Map<String, String> getFIELDS() {
        return FIELDS;
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

    @Override
    public CourseRefData clone() {
        CourseRefData clone = new CourseRefData();
        clone.setNum(this.num);
        clone.setName(this.name);
        clone.setType(this.type);
        clone.setUrl(this.url);
        return clone;
    }
}
