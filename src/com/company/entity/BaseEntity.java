package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mila on 09.05.2017.
 */
public abstract class BaseEntity implements Serializable {

    private String guid;

    private Date createDate;

    private Date lastEditingDate;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastEditingDate() {
        return lastEditingDate;
    }

    public void setLastEditingDate(Date lastEditingDate) {
        this.lastEditingDate = lastEditingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        return getGuid() != null ? getGuid().equals(that.getGuid()) : that.getGuid() == null;

    }

    @Override
    public int hashCode() {
        return getGuid() != null ? getGuid().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "guid='" + guid + '\'' +
                ", createDate=" + createDate +
                ", lastEditingDate=" + lastEditingDate ;
    }
}
