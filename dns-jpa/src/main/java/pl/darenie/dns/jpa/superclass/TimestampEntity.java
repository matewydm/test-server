package pl.darenie.dns.jpa.superclass;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class TimestampEntity {

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @Column(name = "update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updateDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @PrePersist
    public void prePersist() {
        creationDate = new Date();
        updateDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        if (creationDate == null) {
            creationDate = new Date();
        }
        updateDate = new Date();
    }
}

