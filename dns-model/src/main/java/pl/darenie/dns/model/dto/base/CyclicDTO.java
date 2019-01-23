package pl.darenie.dns.model.dto.base;

import pl.darenie.dns.model.enums.CyclicType;

public class CyclicDTO {

    private Long id;
    private CyclicType type;
    private Integer day;
    private Integer hour;
    private Integer minute;

    public CyclicDTO() {}

    protected CyclicDTO(Builder<?> builder) {
        setId(builder.id);
        setType(builder.cyclicType);
        setDay(builder.day);
        setHour(builder.hour);
        setMinute(builder.minute);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


    public static class Builder<T extends Builder<T>>  {
        private Long id;
        private CyclicType cyclicType;
        private Integer day;
        private Integer hour;
        private Integer minute;

        public Builder() {
        }

        public T id(Long val) {
            id = val;
            return (T) this;
        }

        public T cyclicType(CyclicType val) {
            cyclicType = val;
            return (T) this;
        }

        public T day(Integer val) {
            day = val;
            return (T) this;
        }

        public T hour(Integer val) {
            hour = val;
            return (T) this;
        }

        public T minute(Integer val) {
            minute = val;
            return (T) this;
        }

        public CyclicDTO build() {
            return new CyclicDTO((T) this);
        }
    }
}
