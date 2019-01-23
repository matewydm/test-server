package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @Query(value = "INSERT INTO invitation(sender, receiver) VALUES (?1, ?2)", nativeQuery = true)
    int insertInvitation(String senderId, String receiverId);


    @Query("select inv from Invitation inv where (inv.senderToken = ?1 and inv.receiverToken = ?2) or " +
            "(inv.senderToken = ?2 and inv.receiverToken = ?1)")
    Invitation findAllBySenderTokenAndReceiverTokenAnd(String senderToken, String receiverToken);
}
