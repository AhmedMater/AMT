package am.infrastructure.data.view.resultset;

import am.infrastructure.data.view.ui.UserListUI;
import am.main.data.dto.ListResultSet;
import am.main.data.vto.PaginationInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed.motair on 1/1/2018.
 */
public class UserListRS implements Serializable {
    private List<UserListUI> data;
    private PaginationInfo pagination;

    public UserListRS() {
    }
    public UserListRS(ListResultSet<UserListUI> resultSet) {
        this.data = resultSet.getData();
        this.pagination = resultSet.getPagination();
    }

    public List<UserListUI> getData() {
        return data;
    }
    public void setData(List<UserListUI> data) {
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
        if (!(o instanceof UserListRS)) return false;

        UserListRS that = (UserListRS) o;

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
        return "UserListRS{" +
                "data = " + data +
                ", pagination = " + pagination +
                "}\n";
    }
}
