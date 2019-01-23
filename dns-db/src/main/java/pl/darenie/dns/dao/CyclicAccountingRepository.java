package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.CyclicAccounting;
import pl.darenie.dns.model.enums.CyclicType;

import java.util.List;

@Repository
public interface CyclicAccountingRepository extends JpaRepository<CyclicAccounting, Long> {

    CyclicAccounting findByFriendId(Long friendId);

    List<CyclicAccounting> findByTypeAndDayAndHourAndMinute(CyclicType week, int dayOfWeek, Integer hour, Integer minute);
}
