package am.infrastructure.data.view.lookup.list;

import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
public class NewCourseLookup implements Serializable{
    private List<MaterialType> materialTypeList;
    private List<CourseType> courseTypeList;
    private List<CourseLevel> courseLevelList;

    public NewCourseLookup() {
    }
    public NewCourseLookup(List<MaterialType> materialTypeList, List<CourseType> courseTypeList, List<CourseLevel> courseLevelList) {
        this.materialTypeList = materialTypeList;
        this.courseTypeList = courseTypeList;
        this.courseLevelList = courseLevelList;
    }

    public List<MaterialType> getMaterialTypeList() {
        return materialTypeList;
    }
    public void setMaterialTypeList(List<MaterialType> materialTypeList) {
        this.materialTypeList = materialTypeList;
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
        if (!(o instanceof NewCourseLookup)) return false;

        NewCourseLookup that = (NewCourseLookup) o;

        if (getMaterialTypeList() != null ? !getMaterialTypeList().equals(that.getMaterialTypeList()) : that.getMaterialTypeList() != null) return false;
        if (getCourseTypeList() != null ? !getCourseTypeList().equals(that.getCourseTypeList()) : that.getCourseTypeList() != null) return false;
        return getCourseLevelList() != null ? getCourseLevelList().equals(that.getCourseLevelList()) : that.getCourseLevelList() == null;
    }

    @Override
    public int hashCode() {
        int result = getMaterialTypeList() != null ? getMaterialTypeList().hashCode() : 0;
        result = 31 * result + (getCourseTypeList() != null ? getCourseTypeList().hashCode() : 0);
        result = 31 * result + (getCourseLevelList() != null ? getCourseLevelList().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewCourseLookup{" +
                "materialTypeList = " + materialTypeList +
                ", courseTypeList = " + courseTypeList +
                ", courseLevelList = " + courseLevelList +
                "}\n";
    }
}
