package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.CyclicAccounting;
import pl.darenie.dns.jpa.Friend;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {

    Friend findByUserIdAndFriendId(String userId, String FriendId);
}
