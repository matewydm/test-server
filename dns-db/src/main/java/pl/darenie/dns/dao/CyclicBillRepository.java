package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.CyclicBill;
import pl.darenie.dns.model.enums.CyclicType;

import javax.persistence.Entity;
import java.util.List;

@Repository
public interface CyclicBillRepository extends JpaRepository<CyclicBill, Long> {

    List<CyclicBill> findByTypeAndDayAndHourAndMinute(CyclicType type, Integer day, Integer hour, Integer minute);

}

