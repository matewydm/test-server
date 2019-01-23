package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.Bill;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @EntityGraph(value = "Bill.userHasCashBill", type = EntityGraph.EntityGraphType.LOAD)
    Bill findById(Long id);

    @EntityGraph(value = "Bill.userHasCashBill", type = EntityGraph.EntityGraphType.LOAD)
    List<Bill> findDistinctByOwnerId(String id);

    @Query("SELECT DISTINCT uhc.bill FROM UserHasCashBill uhc where uhc.userId = ?1")
    List<Bill> findBillHistory(String userId);
}
