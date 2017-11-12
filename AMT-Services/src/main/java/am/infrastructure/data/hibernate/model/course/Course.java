package am.infrastructure.data.hibernate.model.course;

import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.user.Users;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by ahmed.motair on 9/15/2017.
 */

@Entity
@Cacheable(false)
@Table(name = "course")
public class Course implements Serializable{
    @Id
    @Column(name = "course_id")
    private String courseID;

    @Basic
    @Column(name = "course_name")
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "course_type", referencedColumnName = "type")
    private CourseType courseType;

    @ManyToOne
    @JoinColumn(name = "course_level", referencedColumnName = "level")
    private CourseLevel courseLevel;

    @Basic
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Basic
    @Column(name = "actual_duration")
    private Integer actualDuration;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    private Users createdBy;

    @Basic
    @Column(name = "creation_date")
    private Date creationDate;

    @Basic
    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @Basic
    @Column(name = "is_Completed")
    private Boolean completed;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", insertable = true)
    private Set<CourseReference> references;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", insertable = true)
    private Set<CoursePreRequisite> preRequisites;

    public Course() {
    }
    public Course(String courseName, CourseType courseType, CourseLevel courseLevel, Integer estimatedDuration, Integer actualDuration, String description, Users createdBy, Date creationDate) {
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseLevel = courseLevel;
        this.estimatedDuration = estimatedDuration;
        this.actualDuration = actualDuration;
        this.description = description;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
    }
    public Course(String courseName, CourseType courseType, CourseLevel courseLevel, Integer estimatedDuration, Integer actualDuration, String description, Users createdBy, Date creationDate, Date lastUpdateDate, Boolean completed) {
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseLevel = courseLevel;
        this.estimatedDuration = estimatedDuration;
        this.actualDuration = actualDuration;
        this.description = description;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.completed = completed;
    }

    @PrePersist
    private void prePersist(){
        for (CourseReference reference : references)
            reference.setCourseID(this.courseID);

        for (CoursePreRequisite preRequisite: preRequisites)
            preRequisite.setCourseID(this.courseID);
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

    public CourseType getCourseType() {
        return courseType;
    }
    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }
    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Integer getActualDuration() {
        return actualDuration;
    }
    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Users getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Boolean getCompleted() {
        return completed;
    }
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Set<CourseReference> getReferences() {
        return references;
    }
    public void setReferences(Set<CourseReference> references) {
        this.references = references;
    }

    public Set<CoursePreRequisite> getPreRequisites() {
        return preRequisites;
    }
    public void setPreRequisites(Set<CoursePreRequisite> preRequisites) {
        this.preRequisites = preRequisites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        return courseID != null ? courseID.equals(course.courseID) : course.courseID == null;
    }

    @Override
    public int hashCode() {
        return courseID != null ? courseID.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID = " + courseID +
                ", courseName = " + courseName +
                ", courseType = " + courseType +
                ", courseLevel = " + courseLevel +
                ", estimatedDuration = " + estimatedDuration +
                ", actualDuration = " + actualDuration +
                ", description = " + description +
                ", createdBy = " + createdBy +
                ", creationDate = " + creationDate +
                ", lastUpdateDate = " + lastUpdateDate +
                ", completed = " + completed +
                "}\n";
    }
}
