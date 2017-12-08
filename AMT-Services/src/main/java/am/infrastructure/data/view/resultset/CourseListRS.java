package am.infrastructure.data.view.resultset;

import am.infrastructure.data.view.ui.CourseListUI;
import am.main.data.vto.PaginationInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 12/2/2017.
 */
public class CourseListRS implements Serializable{
    private List<CourseListUI> data;
    public PaginationInfo paginationInfo;

    public CourseListRS() {
    }
    public CourseListRS(List<CourseListUI> data, PaginationInfo paginationInfo) {
        this.data = data;
        this.paginationInfo = paginationInfo;
    }

    public List<CourseListUI> getData() {
        return data;
    }
    public void setData(List<CourseListUI> data) {
        this.data = data;
    }

    public PaginationInfo getPaginationInfo() {
        return paginationInfo;
    }
    public void setPaginationInfo(PaginationInfo paginationInfo) {
        this.paginationInfo = paginationInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListRS)) return false;

        CourseListRS that = (CourseListRS) o;

        if (getData() != null ? !getData().equals(that.getData()) : that.getData() != null) return false;
        return getPaginationInfo() != null ? getPaginationInfo().equals(that.getPaginationInfo()) : that.getPaginationInfo() == null;
    }

    @Override
    public int hashCode() {
        int result = getData() != null ? getData().hashCode() : 0;
        result = 31 * result + (getPaginationInfo() != null ? getPaginationInfo().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseListRS{" +
                "data = " + data +
                ", paginationInfo = " + paginationInfo +
                "}\n";
    }
}
