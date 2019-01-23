package pl.darenie.dns.model.dto;

import pl.darenie.dns.model.enums.CyclicType;

public class CyclicDTO {

    private CyclicType type;
    private Integer day;
    private Integer hour;
    private Integer minute;

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
