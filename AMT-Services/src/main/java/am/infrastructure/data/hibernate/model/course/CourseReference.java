package am.infrastructure.data.hibernate.model.course;


import am.infrastructure.data.dto.course.CourseRefData;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.main.api.db.DBManager;
import am.main.exception.BusinessException;
import am.main.session.AppSession;

import javax.persistence.*;
import java.io.Serializable;

import static am.main.data.enums.impl.IEC.E_DB_7;

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
    @Column(name = "reference_num")
    private Integer num;

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
    public CourseReference(String courseID, Integer num,  String name, MaterialType type, String url) {
        this.courseID = courseID;
        this.num = num;
        this.name = name;
        this.type = type;
        this.url = url;
    }
    public CourseReference(AppSession session, DBManager dbManager, CourseRefData ref) throws Exception{
        MaterialType materialType = dbManager.find(session, MaterialType.class, ref.getType(), true);
        if(materialType != null)
            this.type = materialType;
        else
            throw new BusinessException(session, E_DB_7, MaterialType.class.getSimpleName(), ref.getType());

        this.num = ref.getNum();
        this.name = ref.getName();
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

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
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

        if (getReferenceID() != null ? !getReferenceID().equals(that.getReferenceID()) : that.getReferenceID() != null)
            return false;
        return getNum() != null ? getNum().equals(that.getNum()) : that.getNum() == null;
    }

    @Override
    public int hashCode() {
        int result = getReferenceID() != null ? getReferenceID().hashCode() : 0;
        result = 31 * result + (getNum() != null ? getNum().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseReference{" +
                "referenceID = " + referenceID +
                ", courseID = " + courseID +
                ", num = " + num +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }
}
