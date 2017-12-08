package am.infrastructure.data.view.ui;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ahmed.motair on 12/2/2017.
 */
public class CourseListUI implements Serializable{
    private String courseID;
    private String courseName;
    private String courseLevel;
    private String courseStatus;
    private String estimatedDuration;
    private String actualDuration;
    private String tutor;
    private Date startDate;
    private Float progress;

    public CourseListUI() {
    }
    public CourseListUI(String courseID, String courseName, String courseLevel, String courseStatus, String estimatedDuration, String actualDuration, String tutor, Date startDate, Float progress) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseStatus = courseStatus;
        this.estimatedDuration = estimatedDuration;
        this.actualDuration = actualDuration;
        this.tutor = tutor;
        this.startDate = startDate;
        this.progress = progress;
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

    public String getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }
    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getActualDuration() {
        return actualDuration;
    }
    public void setActualDuration(String actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getTutor() {
        return tutor;
    }
    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Float getProgress() {
        return progress;
    }
    public void setProgress(Float progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListUI)) return false;

        CourseListUI that = (CourseListUI) o;

        return getCourseID() != null ? getCourseID().equals(that.getCourseID()) : that.getCourseID() == null;
    }

    @Override
    public int hashCode() {
        return getCourseID() != null ? getCourseID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CourseListUI{" +
                "courseID = " + courseID +
                ", courseName = " + courseName +
                ", courseLevel = " + courseLevel +
                ", courseStatus = " + courseStatus +
                ", estimatedDuration = " + estimatedDuration +
                ", actualDuration = " + actualDuration +
                ", tutor = " + tutor +
                ", startDate = " + startDate +
                ", progress = " + progress +
                "}\n";
    }
}
