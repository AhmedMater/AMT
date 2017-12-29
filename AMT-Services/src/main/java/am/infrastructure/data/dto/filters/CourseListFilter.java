package am.infrastructure.data.dto.filters;

import am.infrastructure.data.hibernate.model.course.Course;
import am.main.data.dto.filter.SortingInfo;

import java.io.Serializable;

/**
 * Created by ahmed.motair on 12/16/2017.
 */
public class CourseListFilter implements Serializable {
    private String courseName;
    private String courseLevel;
    private String courseType;
    private Integer pageNum;
    private SortingInfo sortingInfo;

    public CourseListFilter() {
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLevel() {
        return courseLevel;
    }
    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseType() {
        return courseType;
    }
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public SortingInfo getSortingInfo() {
        return (sortingInfo == null) ? new SortingInfo(Course.CREATION_DATE) : sortingInfo;
    }
    public void setSortingInfo(SortingInfo sortingInfo) {
        this.sortingInfo = sortingInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListFilter)) return false;

        CourseListFilter that = (CourseListFilter) o;

        if (getCourseName() != null ? !getCourseName().equals(that.getCourseName()) : that.getCourseName() != null) return false;
        if (getCourseLevel() != null ? !getCourseLevel().equals(that.getCourseLevel()) : that.getCourseLevel() != null) return false;
        return getCourseType() != null ? getCourseType().equals(that.getCourseType()) : that.getCourseType() == null;
    }

    @Override
    public int hashCode() {
        int result = getCourseName() != null ? getCourseName().hashCode() : 0;
        result = 31 * result + (getCourseLevel() != null ? getCourseLevel().hashCode() : 0);
        result = 31 * result + (getCourseType() != null ? getCourseType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseListFilter{" +
                "courseName = " + courseName +
                ", courseLevel = " + courseLevel +
                ", courseType = " + courseType +
                "}\n";
    }
}
