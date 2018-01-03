package am.infrastructure.data.dto.course;

import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.course.CoursePreRequisite;
import am.infrastructure.data.hibernate.model.course.CourseReference;
import am.main.api.validation.groups.*;
import am.main.common.RegExp;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseData implements Serializable{
    private static final Map<String, String> FIELDS = Collections.unmodifiableMap(
        new HashMap<String, String>(){{
            put("courseName", "Course Name");
            put("courseLevel", "Course Level");
            put("courseType", "Course Type");
            put("description", "Description");
            put("estimatedDuration", "Estimated Duration");
            put("estimatedMinPerDay", "Estimated Min-Per-Day");
        }}
    );
    private String courseID;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 5, max = 100, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.CONTENT_NAME, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String courseName;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String courseLevel;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Length(min = 2, max = 2, message = FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
    @Pattern(regexp = RegExp.LOOKUP, message = FormValidation.REGEX, groups = InvalidValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String courseType;

    @Length(max = 200, message = FormValidation.MAX_LENGTH, groups = LengthValidation.class)
    @NotBlank(message = FormValidation.EMPTY_STR, groups = BlankValidation.class)
    private String description;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Positive(message = FormValidation.POSITIVE_NUM, groups = InvalidValidation.class)
    @Min(value = 5, message = FormValidation.MIN_VALUE, groups = InvalidValidation.class)
    private Integer estimatedDuration;

    @NotNull(message = FormValidation.REQUIRED, groups = RequiredValidation.class)
    @Positive(message = FormValidation.POSITIVE_NUM, groups = InvalidValidation.class)
    @Min(value = 10, message = FormValidation.MIN_VALUE, groups = InvalidValidation.class)
    private Integer estimatedMinPerDay;

    private Date startDate;
    private Integer actualDuration;
    private Date createdOn;
    private String createdBy;
    private String courseStatus;
    private Date dueDate;

    private List<CoursePRData> preRequisites;
    private List<CourseRefData> references;

    public CourseData() {
    }
    public CourseData(String courseName, String courseLevel, String courseType, Integer estimatedDuration,
              String courseStatus, String description, List<CoursePRData> preRequisites, List<CourseRefData> references) {
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseType = courseType;
        this.estimatedDuration = estimatedDuration;
        this.description = description;
        this.preRequisites = preRequisites;
        this.references = references;
        this.courseStatus = courseStatus;
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
        this.courseStatus = course.getCourseStatus().getDescription();

        this.preRequisites = new ArrayList<>();
        for (CoursePreRequisite preReq : course.getPreRequisites())
            this.preRequisites.add(new CoursePRData(preReq));

        this.references = new ArrayList<>();
        for (CourseReference ref : course.getReferences())
            this.references.add(new CourseRefData(ref));
    }

    public String getCourseID() {
        return courseID;
    }
    public void setCourseID(String courseID) {
        this.courseID = courseID;
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

    public Integer getEstimatedMinPerDay() {
        return estimatedMinPerDay;
    }
    public void setEstimatedMinPerDay(Integer estimatedMinPerDay) {
        this.estimatedMinPerDay = estimatedMinPerDay;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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

    public String getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public static Map<String, String> getFIELDS() {
        return FIELDS;
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

    @Override
    public CourseData clone(){
        CourseData clone = new CourseData();
        clone.setCourseName(this.courseName);
        clone.setCourseType(this.courseType);
        clone.setCourseLevel(this.courseLevel);
        clone.setEstimatedDuration(this.estimatedDuration);
        clone.setEstimatedMinPerDay(this.estimatedMinPerDay);
        clone.setCourseStatus(this.courseStatus);
        clone.setDescription(this.description);
        clone.setActualDuration(this.actualDuration);
        clone.setCreatedBy(this.createdBy);
        clone.setCreatedOn(this.createdOn);
        clone.setStartDate(this.startDate);
        clone.setDueDate(this.dueDate);

        List<CourseRefData> courseRefDataList = new ArrayList<>();
        if(this.references != null){
            for (CourseRefData reference : this.references) {
                courseRefDataList.add(reference.clone());
            }
        }
        clone.setReferences(courseRefDataList);

        List<CoursePRData> coursePRDataList = new ArrayList<>();
        if(this.preRequisites != null){
            for (CoursePRData preRequisite : this.preRequisites) {
                coursePRDataList.add(preRequisite.clone());
            }
        }
        clone.setPreRequisites(coursePRDataList);
        return clone;
    }
}
