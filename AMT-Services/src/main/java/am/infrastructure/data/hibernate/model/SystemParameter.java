package am.infrastructure.data.hibernate.model;

import am.infrastructure.data.hibernate.model.lookup.DataType;
import am.infrastructure.generic.ConfigParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
@Entity
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = ConfigParam.EH_CACHE_READ_WRITE_REGION)
@Table(name = "system_parameter")
public class SystemParameter implements Serializable{
    public static final String PARAM_NAME = "paramName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "param_id")
    private Integer paramID;

    @Basic
    @Column(name = "param_name")
    private String paramName;

    @Basic
    @Column(name = "param_value")
    private String paramValue;

    @ManyToOne
    @JoinColumn(name = "param_type", referencedColumnName = "type")
    private DataType paramType;

    public SystemParameter() {
    }

    public SystemParameter(String paramName, String paramValue, DataType paramType) {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.paramType = paramType;
    }

    public Integer getParamID() {
        return paramID;
    }
    public void setParamID(Integer paramID) {
        this.paramID = paramID;
    }

    public String getParamName() {
        return paramName;
    }
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public DataType getParamType() {
        return paramType;
    }
    public void setParamType(DataType paramType) {
        this.paramType = paramType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemParameter)) return false;

        SystemParameter that = (SystemParameter) o;

        return getParamID() != null ? getParamID().equals(that.getParamID()) : that.getParamID() == null;
    }

    @Override
    public int hashCode() {
        return getParamID() != null ? getParamID().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SystemParameter{" +
                "paramID = " + paramID +
                ", paramName = " + paramName +
                ", paramValue = " + paramValue +
                ", paramType = " + paramType +
                "}\n";
    }
}
