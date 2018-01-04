package am.infrastructure.data.dto.filters;

import am.infrastructure.data.hibernate.model.course.Course;
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
 * Created by ahmed.motair on 12/16/2017.
 */
public class CourseListFilter implements Serializable {
    public static final Map<String, String> FIELDS = Collections.unmodifiableMap(
            new HashMap<String, String>(){{
                put("courseName", "Course Name");
                put("courseLevel", "Course Level");
                put("courseType", "Course Type");
                put("pageNum", "Page Number");
                put("sorting", "Sorting Info");
            }}
    );

    @NullAndNotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    @Length(max = 100, message = FormValidation.MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.CONTENT_NAME, message = FormValidation.REGEX, groups = InvalidValidation.class)
    private String courseName;

    @NullAndNotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.EQ_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    private String courseLevel;

    @NullAndNotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.EQ_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    private String courseType;

    private Date creationDateFrom;
    private Date creationDateTo;
    private Date startDateFrom;
    private Date startDateTo;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @PositiveOrZero(message = FormValidation.POSITIVE_NUM_AND_ZERO, groups = InvalidValidation.class)
    private Integer pageNum;

    private SortingInfo sorting;

    public CourseListFilter() {
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLevel() {
        return courseLevel;
    }
    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseType() {
        return courseType;
    }
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public SortingInfo getSorting() {
        return (sorting == null) ? new SortingInfo(Course.CREATION_DATE) : sorting;
    }
    public void setSorting(SortingInfo sorting) {
        this.sorting = sorting;
    }

    public static Map<String, String> getFIELDS() {
        return FIELDS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListFilter)) return false;

        CourseListFilter that = (CourseListFilter) o;

        if (getCourseName() != null ? !getCourseName().equals(that.getCourseName()) : that.getCourseName() != null) return false;
        if (getCourseLevel() != null ? !getCourseLevel().equals(that.getCourseLevel()) : that.getCourseLevel() != null) return false;
        return getCourseType() != null ? getCourseType().equals(that.getCourseType()) : that.getCourseType() == null;
    }

    @Override
    public int hashCode() {
        int result = getCourseName() != null ? getCourseName().hashCode() : 0;
        result = 31 * result + (getCourseLevel() != null ? getCourseLevel().hashCode() : 0);
        result = 31 * result + (getCourseType() != null ? getCourseType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseListFilter{" +
                "courseName = " + courseName +
                ", courseLevel = " + courseLevel +
                ", courseType = " + courseType +
                "}\n";
    }
}
