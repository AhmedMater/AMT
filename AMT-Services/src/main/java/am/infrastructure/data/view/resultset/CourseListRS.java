package am.infrastructure.data.view.resultset;

import am.infrastructure.data.view.ui.CourseListUI;
import am.main.data.dto.ListResultSet;
import am.main.data.vto.PaginationInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 12/2/2017.
 */
public class CourseListRS implements Serializable{
    private List<CourseListUI> data;
    public PaginationInfo pagination;

    public CourseListRS() {
    }
    public CourseListRS(ListResultSet<CourseListUI> resultSet) {
        this.data = resultSet.getData();
        this.pagination = resultSet.getPagination();
    }

    public List<CourseListUI> getData() {
        return data;
    }
    public void setData(List<CourseListUI> data) {
        this.data = data;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }
    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseListRS)) return false;

        CourseListRS that = (CourseListRS) o;

        if (getData() != null ? !getData().equals(that.getData()) : that.getData() != null) return false;
        return getPagination() != null ? getPagination().equals(that.getPagination()) : that.getPagination() == null;
    }

    @Override
    public int hashCode() {
        int result = getData() != null ? getData().hashCode() : 0;
        result = 31 * result + (getPagination() != null ? getPagination().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseListRS{" +
                "data = " + data +
                ", pagination = " + pagination +
                "}\n";
    }
}
