package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.Settlement;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.model.enums.BillPriority;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.enums.Visibility;

import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    List<Settlement> findByChargerIdAndStatusAndVisibility(String userToken, SettlementStatus status, Visibility visibility);

    List<Settlement> findByPayerIdAndStatusAndVisibility(String userToken, SettlementStatus status, Visibility visibility);

    List<Settlement> findByChargerIdAndPayerIdAndVisibility(String userId, String friendId, Visibility visibility);

    List<Settlement> findByChargerAndStatus(User user, SettlementStatus status);

    List<Settlement> findByPayerAndStatus(User user, SettlementStatus status);

    List<Settlement> findByStatus(SettlementStatus status);

    List<Settlement> findByChargerAndPayerAndStatus(User user, User friend, SettlementStatus unpaid);

    List<Settlement> findByBillId(Long billId);
}