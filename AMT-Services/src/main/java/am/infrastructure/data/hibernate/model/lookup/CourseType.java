package am.infrastructure.data.hibernate.model.lookup;

import am.infrastructure.generic.ConfigParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/5/2017.
 */
@Entity
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = ConfigParam.EH_CACHE_LOOKUP_REGION)
@Table(name = "course_type")
public class CourseType implements Serializable {
    @Id
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "description")
    private String description;

    public CourseType() {
    }
    public CourseType(String type, String description) {
        this.type = type;
        this.description = description;
    }
    public CourseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseType)) return false;

        CourseType that = (CourseType) o;

        return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
    }

    @Override
    public int hashCode() {
        return getType() != null ? getType().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CourseType{" +
                "type = " + type +
                ", description = " + description +
                "}\n";
    }
}
