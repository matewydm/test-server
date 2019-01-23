package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.model.enums.BillPriority;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.enums.Visibility;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT DISTINCT s.charger FROM Settlement s JOIN s.bill b WHERE b.priority IN (?1) AND s.status = ?2 AND s.visibility = ?3")
    List<User> findChargerByBillPriorityAndStatusAndVisibility(List<BillPriority> priorityList, SettlementStatus unpaid, Visibility visible);

}
