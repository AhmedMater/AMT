package am.infrastructure.data.view.lookup.list;

import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 12/26/2017.
 */
public class CourseListFilters implements Serializable{
    private List<CourseType> courseTypeList;
    private List<CourseLevel> courseLevelList;

    public CourseListFilters() {
    }
    public CourseListFilters(List<CourseType> courseTypeList, List<CourseLevel> courseLevelList) {
        this.courseTypeList = courseTypeList;
        this.courseLevelList = courseLevelList;
    }

    public List<CourseType> getCourseTypeList() {
        return courseTypeList;
    }
    public void setCourseTypeList(List<CourseType> courseTypeList) {
        this.courseTypeList = courseTypeList;
    }

    public List<CourseLevel> getCourseLevelList() {
        return courseLevelList;
    }
    public void setCourseLevelList(List<CourseLevel> courseLevelList) {
        this.courseLevelList = courseLevelList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListFilters)) return false;

        CourseListFilters that = (CourseListFilters) o;

        if (getCourseTypeList() != null ? !getCourseTypeList().equals(that.getCourseTypeList()) : that.getCourseTypeList() != null) return false;
        return getCourseLevelList() != null ? getCourseLevelList().equals(that.getCourseLevelList()) : that.getCourseLevelList() == null;
    }

    @Override
    public int hashCode() {
        int result = getCourseTypeList() != null ? getCourseTypeList().hashCode() : 0;
        result = 31 * result + (getCourseLevelList() != null ? getCourseLevelList().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseListFilters{" +
                "courseTypeList = " + courseTypeList +
                ", courseLevelList = " + courseLevelList +
                "}\n";
    }
}
