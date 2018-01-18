package am.infrastructure.data.hibernate.model.lookup;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/30/2017.
 */
@Entity
@Table(name = "content_status")
public class ContentStatus implements Serializable{
    public static final String STATUS = "status";

    @Id
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "description")
    private String description;

    public ContentStatus() {
    }
    public ContentStatus(String status) {
        this.status = status;
    }
    public ContentStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
        if (!(o instanceof ContentStatus)) return false;

        ContentStatus that = (ContentStatus) o;

        return getStatus() != null ? getStatus().equals(that.getStatus()) : that.getStatus() == null;
    }

    @Override
    public int hashCode() {
        return getStatus() != null ? getStatus().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ContentStatus{" +
                "status = " + status +
                ", description = " + description +
                "}\n";
    }
}
