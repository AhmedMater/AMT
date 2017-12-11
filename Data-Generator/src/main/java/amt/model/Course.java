package amt.model;

import amt.generic.Gen;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahmed.motair on 12/11/2017.
 */
public class Course {
    private String courseID;
    private String courseName;
    private String courseLevel;
    private String courseType;
    private Integer estimatedDuration;
    private String description;
    private Integer estimatedMinPerDay;
    private String courseStatus;
    private Integer createdBy;
    private Date createdOn;
    private Float progress;

    public Course() {
    }
    public Course(String courseID, String courseName, String courseLevel, String courseType, Integer estimatedDuration,
                  String description, Integer estimatedMinPerDay, String courseStatus, Integer createdBy, Date createdOn,
                  Float progress) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseType = courseType;
        this.estimatedDuration = estimatedDuration;
        this.description = description;
        this.estimatedMinPerDay = estimatedMinPerDay;
        this.courseStatus = courseStatus;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.progress = progress;
    }

    public String getCourseID() {
        return courseID;
    }
    public void setCourseID(Integer courseNum) {
        String courseNumStr = (courseNum.toString().length() != 7) ?
            StringUtils.repeat("0", 7 - courseNum.toString().length()) + courseNum.toString() : courseNum.toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        this.courseID = "Cor" + courseNumStr + courseType + courseLevel + df.format(createdOn);
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        if(courseName.length()>100)
            this.courseName = courseName.substring(0,100);
        else
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

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        if(description.length()>200)
            this.description = description.substring(0,200);
        else
            this.description = description;
    }

    public Integer getEstimatedMinPerDay() {
        return estimatedMinPerDay;
    }
    public void setEstimatedMinPerDay(Integer estimatedMinPerDay) {
        this.estimatedMinPerDay = estimatedMinPerDay;
    }

    public String getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Float getProgress() {
        return progress;
    }
    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public String generateSQL()throws Exception{
        String INSERT_STATEMENT = "INSERT INTO course (course_id, course_name, course_level, course_type, " +
                "course_status, estimated_duration, description, min_per_day, created_by, creation_date,  progress) " +
                "VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'', ''{8}'', ''{9}'', ''{10}'');";

        return MessageFormat.format(INSERT_STATEMENT, courseID, courseName, courseLevel, courseType, courseStatus,
                estimatedDuration.toString(), Gen.escapeSingleQuote(description), estimatedMinPerDay.toString(),
                createdBy.toString(), Gen.formatDate(createdOn), progress.toString());
    }

}
