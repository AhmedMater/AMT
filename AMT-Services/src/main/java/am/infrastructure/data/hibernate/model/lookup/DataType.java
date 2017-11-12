package am.infrastructure.data.hibernate.model.lookup;

import am.infrastructure.generic.ConfigParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
@Entity
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = ConfigParam.EH_CACHE_LOOKUP_REGION)
@Table(name = "data_type")
public class DataType implements Serializable {
    @Id
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "regex")
    private String regex;

    public DataType() {
    }

    public DataType(String type, String description, String regex) {
        this.type = type;
        this.description = description;
        this.regex = regex;
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

    public String getRegex() {
        return regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataType)) return false;

        DataType dataType = (DataType) o;

        return getType() != null ? getType().equals(dataType.getType()) : dataType.getType() == null;
    }

    @Override
    public int hashCode() {
        return getType() != null ? getType().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "type = " + type +
                ", description = " + description +
                ", regex = " + regex +
                "}\n";
    }
}
