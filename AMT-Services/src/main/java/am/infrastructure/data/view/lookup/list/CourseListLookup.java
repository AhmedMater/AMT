package am.infrastructure.data.view.lookup.list;

import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 12/26/2017.
 */
public class CourseListLookup implements Serializable{
    private List<CourseType> courseTypes;
    private List<CourseLevel> courseLevels;

    public CourseListLookup() {
    }
    public CourseListLookup(List<CourseType> courseTypes, List<CourseLevel> courseLevels) {
        this.courseTypes = courseTypes;
        this.courseLevels = courseLevels;
    }

    public List<CourseType> getCourseTypes() {
        return courseTypes;
    }
    public void setCourseTypes(List<CourseType> courseTypes) {
        this.courseTypes = courseTypes;
    }

    public List<CourseLevel> getCourseLevels() {
        return courseLevels;
    }
    public void setCourseLevels(List<CourseLevel> courseLevels) {
        this.courseLevels = courseLevels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListLookup)) return false;

        CourseListLookup that = (CourseListLookup) o;

        if (getCourseTypes() != null ? !getCourseTypes().equals(that.getCourseTypes()) : that.getCourseTypes() != null)
            return false;
        return getCourseLevels() != null ? getCourseLevels().equals(that.getCourseLevels()) : that.getCourseLevels() == null;
    }

    @Override
    public int hashCode() {
        int result = getCourseTypes() != null ? getCourseTypes().hashCode() : 0;
        result = 31 * result + (getCourseLevels() != null ? getCourseLevels().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseListLookup{" +
                "courseTypes = " + courseTypes +
                ", courseLevels = " + courseLevels +
                "}\n";
    }
}
