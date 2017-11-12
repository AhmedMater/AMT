package am.infrastructure.data.dto.course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseData implements Serializable{
    private String courseName;
    private String courseLevel;
    private String courseType;
    private Integer estimatedDuration;
    private String description;

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
