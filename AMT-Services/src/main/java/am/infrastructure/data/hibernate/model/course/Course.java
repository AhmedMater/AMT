package am.infrastructure.data.hibernate.model.course;

import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.course.CoursePRData;
import am.infrastructure.data.dto.course.CourseRefData;
import am.infrastructure.data.hibernate.model.lookup.ContentStatus;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.db.DBManager;
import am.main.exception.BusinessException;
import am.main.session.AppSession;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static am.shared.enums.EC.AMT_0002;

/**
 * Created by ahmed.motair on 9/15/2017.
 */

@Entity
@Cacheable(false)
@Table(name = "course")
public class Course implements Serializable{
    public static final String COURSE_CREATOR_USER_ID = "createdBy." + Users.USER_ID;
    public static final String CREATION_DATE = "creationDate";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_TYPE = "courseType." + CourseType.TYPE;
    public static final String COURSE_LEVEL = "courseLevel." + CourseLevel.LEVEL;

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
    @Column(name = "start_date")
    private Date startDate;

    @Basic
    @Column(name = "min_per_day")
    private Integer minPerDay;

    @Basic
    @Column(name = "due_date")
    private Date dueDate;

    @Basic
    @Column(name = "progress")
    private Float progress;

    @ManyToOne
    @JoinColumn(name = "course_status", referencedColumnName = "status")
    private ContentStatus courseStatus;

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
    public Course(String courseName, CourseType courseType, CourseLevel courseLevel, Integer estimatedDuration, Integer actualDuration, String description, Users createdBy, Date creationDate, Date lastUpdateDate, ContentStatus courseStatus) {
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseLevel = courseLevel;
        this.estimatedDuration = estimatedDuration;
        this.actualDuration = actualDuration;
        this.description = description;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.courseStatus = courseStatus;
    }

    public Course(AppSession session, DBManager dbManager, CourseData courseData) throws Exception{
        CourseType courseType = dbManager.find(session, CourseType.class, courseData.getCourseType(), true);
        if(courseType != null)
            this.courseType = courseType;
        else
            throw new BusinessException(session, AMT_0002, CourseType.class.getSimpleName(), courseData.getCourseType());

        CourseLevel courseLevel = dbManager.find(session, CourseLevel.class, courseData.getCourseLevel(), true);
        if(courseLevel != null)
            this.courseLevel = courseLevel;
        else
            throw new BusinessException(session, AMT_0002, CourseLevel.class.getSimpleName(), courseData.getCourseLevel());

        this.references = new HashSet<>();
        for (CourseRefData ref : courseData.getReferences())
            this.references.add(new CourseReference(session, dbManager, ref));

        this.preRequisites = new HashSet<>();
        for (CoursePRData preReq : courseData.getPreRequisites())
            this.preRequisites.add(new CoursePreRequisite(session, dbManager, preReq));

        this.courseName = courseData.getCourseName();
        this.description = courseData.getDescription();
        this.estimatedDuration = courseData.getEstimatedDuration();
        this.minPerDay = courseData.getEstimatedMinPerDay();
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

    public Integer getMinPerDay() {
        return minPerDay;
    }
    public void setMinPerDay(Integer minPerDay) {
        this.minPerDay = minPerDay;
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

    public ContentStatus getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(ContentStatus courseStatus) {
        this.courseStatus = courseStatus;
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

    public Float getProgress() {
        return progress;
    }
    public void setProgress(Float progress) {
        this.progress = progress;
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
                ", courseStatus = " + courseStatus +
                "}\n";
    }
}
