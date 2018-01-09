package amt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ahmed.motair on 1/6/2018.
 */
@Entity
@Table(name = "logger")
public class UserLog {
    @Id
    @Column(name = "log_id")
    private String logID;

    public UserLog() {
    }

    public UserLog(String logID) {
        this.logID = logID;
    }

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLog)) return false;

        UserLog userLog = (UserLog) o;

        return getLogID() != null ? getLogID().equals(userLog.getLogID()) : userLog.getLogID() == null;
    }

    @Override
    public int hashCode() {
        return getLogID() != null ? getLogID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserLog{" +
                    "logID = " + logID +
                "}\n";
    }
}

