package am.infrastructure.data.hibernate.model.course;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
@Entity
@Cacheable(false)
@Table(name = "course_pre_requisite")
public class CoursePreRequisite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pre_requisite_id")
    private Integer preRequisiteID;

    @Basic
    @Column(name = "course_id")
    private String courseID;

    @Basic
    @Column(name = "pre_requisite_name")
    private String name;

    @Basic
    @Column(name = "pre_requisite_type")
    private String type;

    @Basic
    @Column(name = "pre_requisite_url")
    private String url;

    public CoursePreRequisite() {
    }

    public CoursePreRequisite(String courseID, String name, String type, String url) {
        this.courseID = courseID;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public Integer getPreRequisiteID() {
        return preRequisiteID;
    }
    public void setPreRequisiteID(Integer preRequisiteID) {
        this.preRequisiteID = preRequisiteID;
    }

    public String getCourseID() {
        return courseID;
    }
    public void setCourseID(String courseID) {
        this.courseID = courseID;
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
        if (!(o instanceof CoursePreRequisite)) return false;

        CoursePreRequisite that = (CoursePreRequisite) o;

        return getPreRequisiteID() != null ? getPreRequisiteID().equals(that.getPreRequisiteID()) : that.getPreRequisiteID() == null;
    }

    @Override
    public int hashCode() {
        return getPreRequisiteID() != null ? getPreRequisiteID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CoursePreRequisite{" +
                "preRequisiteID = " + preRequisiteID +
                ", courseID = " + courseID +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }
}
