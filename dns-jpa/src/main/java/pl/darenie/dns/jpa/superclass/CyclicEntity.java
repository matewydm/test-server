package pl.darenie.dns.jpa.superclass;

import pl.darenie.dns.model.enums.CyclicType;

import javax.persistence.*;

@MappedSuperclass
public class CyclicEntity extends TimestampEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    protected CyclicType type;

    @Column(name = "day", nullable = false)
    protected Integer day;

    @Column(name = "hour", nullable = false)
    protected Integer hour;

    @Column(name = "minute", nullable = false)
    protected Integer minute;

    public CyclicType getType() {
        return type;
    }

    public void setType(CyclicType type) {
        this.type = type;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

}
