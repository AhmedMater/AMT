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
@Table(name = "course_level")
public class CourseLevel implements Serializable{
    public static final String LEVEL = "level";
    @Id
    @Column(name = "level")
    private String level;

    @Basic
    @Column(name = "description")
    private String description;

    public CourseLevel() {
    }
    public CourseLevel(String level, String description) {
        this.level = level;
        this.description = description;
    }
    public CourseLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
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
        if (!(o instanceof CourseLevel)) return false;

        CourseLevel that = (CourseLevel) o;

        return getLevel() != null ? getLevel().equals(that.getLevel()) : that.getLevel() == null;
    }

    @Override
    public int hashCode() {
        return getLevel() != null ? getLevel().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CourseLevel{" +
                "level = " + level +
                ", description = " + description +
                "}\n";
    }
}
