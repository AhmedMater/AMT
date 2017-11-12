package am.infrastructure.data.dto.course;

import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseRefData implements Serializable {
    private Integer num;
    private String name;
    private String type;
    private String url;

    public CourseRefData() {
    }
    public CourseRefData(Integer num, String name, String type, String url) {
        this.num = num;
        this.name = name;
        this.type = type;
        this.url = url;
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
        if (!(o instanceof CourseRefData)) return false;

        CourseRefData that = (CourseRefData) o;

        if (getNum() != null ? !getNum().equals(that.getNum()) : that.getNum() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        return getUrl() != null ? getUrl().equals(that.getUrl()) : that.getUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getNum() != null ? getNum().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseRefData{" +
                "num = " + num +
                ", name = " + name +
                ", type = " + type +
                ", url = " + url +
                "}\n";
    }
}
