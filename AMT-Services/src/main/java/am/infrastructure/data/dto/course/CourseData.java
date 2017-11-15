package am.infrastructure.data.dto.course;

import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.course.CoursePreRequisite;
import am.infrastructure.data.hibernate.model.course.CourseReference;
import am.common.validation.RegExp;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static am.common.ValidationErrorMsg.*;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseData implements Serializable{
    @NotNull(message = COURSE_NAME.REQUIRED)
    @NotEmpty(message = COURSE_NAME.EMPTY_STR)
    @Length(min = 5, max = 100, message = COURSE_NAME.LENGTH)
    @Pattern(regexp = RegExp.CONTENT_NAME, message = COURSE_NAME.INVALID)
    private String courseName;

    @NotNull(message = COURSE_LEVEL.REQUIRED)
    @NotEmpty(message = COURSE_LEVEL.EMPTY_STR)
    @Length(min = 2, max = 2, message = COURSE_LEVEL.LENGTH)
    private String courseLevel;

    @NotNull(message = COURSE_TYPE.REQUIRED)
    @NotEmpty(message = COURSE_TYPE.EMPTY_STR)
    @Length(min = 2, max = 2, message = COURSE_TYPE.LENGTH)
    private String courseType;

    @NotEmpty(message = COURSE_DESCRIPTION.EMPTY_STR)
    @Length(max = 200, message = COURSE_DESCRIPTION.LENGTH)
    private String description;

    @NotNull(message = COURSE_DURATION.REQUIRED)
    @Min(value = 1, message = COURSE_DURATION.INVALID)
    private Integer estimatedDuration;

    @Min(value = 1, message = COURSE_DURATION.INVALID)
    private Integer actualDuration;

    private Date createdOn;
    private String createdBy;
    private Boolean isCompleted;

    private List<CoursePRData> preRequisites;
    private List<CourseRefData> references;

    public CourseData() {
    }
    public CourseData(String courseName, String courseLevel, String courseType, Integer estimatedDuration, String description, List<CoursePRData> preRequisites, List<CourseRefData> references) {
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseType = courseType;
        this.estimatedDuration = estimatedDuration;
        this.description = description;
        this.preRequisites = preRequisites;
        this.references = references;
    }
    public CourseData(Course course) {
        this.courseName = course.getCourseName();
        this.courseLevel = course.getCourseLevel().getDescription();
        this.courseType = course.getCourseType().getDescription();
        this.description = course.getDescription();

        this.estimatedDuration = course.getEstimatedDuration();
        this.actualDuration = course.getActualDuration();

        this.createdBy = course.getCreatedBy().getFullName();
        this.createdOn = course.getCreationDate();
        this.isCompleted = course.getCompleted();

        this.preRequisites = new ArrayList<>();
        for (CoursePreRequisite preReq : course.getPreRequisites())
            this.preRequisites.add(new CoursePRData(preReq));

        this.references = new ArrayList<>();
        for (CourseReference ref : course.getReferences())
            this.references.add(new CourseRefData(ref));
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public List<CoursePRData> getPreRequisites() {
        return preRequisites;
    }
    public void setPreRequisites(List<CoursePRData> preRequisites) {
        this.preRequisites = preRequisites;
    }

    public List<CourseRefData> getReferences() {
        return references;
    }
    public void setReferences(List<CourseRefData> references) {
        this.references = references;
    }

    public Integer getActualDuration() {
        return actualDuration;
    }
    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }
    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseData)) return false;

        CourseData that = (CourseData) o;

        return getCourseName() != null ? getCourseName().equals(that.getCourseName()) : that.getCourseName() == null;
    }

    @Override
    public int hashCode() {
        return getCourseName() != null ? getCourseName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CourseData{" +
                "courseName = " + courseName +
                ", courseLevel = " + courseLevel +
                ", courseType = " + courseType +
                ", estimatedDuration = " + estimatedDuration +
                ", description = " + description +
                ", preRequisites = " + preRequisites +
                ", references = " + references +
                "}\n";
    }
}
