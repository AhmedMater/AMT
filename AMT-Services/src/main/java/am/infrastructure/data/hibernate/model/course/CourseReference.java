package am.infrastructure.data.hibernate.model.course;


import am.infrastructure.data.dto.course.CourseRefData;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
@Entity
@Cacheable(false)
@Table(name = "course_reference")
public class CourseReference implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_id")
    private Integer referenceID;

    @Basic
    @Column(name = "course_id")
    private String courseID;

    @Basic
    @Column(name = "reference_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "reference_type", referencedColumnName = "type")
    private MaterialType type;

    @Basic
    @Column(name = "reference_url")
    private String url;

    public CourseReference() {
    }
    public CourseReference(String courseID, String name, MaterialType type, String url) {
        this.courseID = courseID;
        this.name = name;
        this.type = type;
        this.url = url;
    }
    public CourseReference(CourseRefData ref) {
        this.name = ref.getName();
        this.type = new MaterialType(ref.getType());
        this.url = ref.getUrl();
    }

    public Integer getReferenceID() {
        return referenceID;
    }
    public void setReferenceID(Integer referenceID) {
        this.referenceID = referenceID;
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

    public MaterialType getType() {
        return type;
    }
    public void setType(MaterialType type) {
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
        if (!(o instanceof CourseReference)) return false;

        CourseReference that = (CourseReference) o;

        return getReferenceID() != null ? getReferenceID().equals(that.getReferenceID()) : that.getReferenceID() == null;
    }

    @Override
    public int hashCode() {
        return getReferenceID() != null ? getReferenceID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CourseReference{" +
                "referenceID = " + referenceID +
                ", courseID = " + courseID +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }
}
