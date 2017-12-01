package am.infrastructure.data.hibernate.model.course;

import am.infrastructure.data.dto.course.CoursePRData;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.main.api.db.DBManager;
import am.main.exception.BusinessException;
import am.main.session.AppSession;

import javax.persistence.*;
import java.io.Serializable;

import static am.shared.enums.EC.AMT_0002;

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
    @Column(name = "pre_requisite_num")
    private Integer num;

    @Basic
    @Column(name = "pre_requisite_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "pre_requisite_type", referencedColumnName = "type")
    private MaterialType type;

    @Basic
    @Column(name = "pre_requisite_url")
    private String url;

    public CoursePreRequisite() {
    }
    public CoursePreRequisite(String courseID, Integer num, String name, MaterialType type, String url) {
        this.courseID = courseID;
        this.num = num;
        this.name = name;
        this.type = type;
        this.url = url;
    }
    public CoursePreRequisite(AppSession session, DBManager dbManager, CoursePRData preReq) throws Exception{
        MaterialType materialType = dbManager.find(session, MaterialType.class, preReq.getType(), true);
        if(materialType != null)
            this.type = materialType;
        else
            throw new BusinessException(session, AMT_0002, MaterialType.class.getSimpleName(), preReq.getType());

        this.num = preReq.getNum();
        this.name = preReq.getName();
        this.url = preReq.getUrl();

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
        if (!(o instanceof CoursePreRequisite)) return false;

        CoursePreRequisite that = (CoursePreRequisite) o;

        if (getPreRequisiteID() != null ? !getPreRequisiteID().equals(that.getPreRequisiteID()) : that.getPreRequisiteID() != null) return false;
        return getNum() != null ? getNum().equals(that.getNum()) : that.getNum() == null;
    }

    @Override
    public int hashCode() {
        int result = getPreRequisiteID() != null ? getPreRequisiteID().hashCode() : 0;
        result = 31 * result + (getNum() != null ? getNum().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoursePreRequisite{" +
                "preRequisiteID = " + preRequisiteID +
                ", courseID = " + courseID +
                ", num = " + num +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }
}
