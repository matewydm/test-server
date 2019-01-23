package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.UserHasGroup;
import pl.darenie.dns.jpa.embeddable.UserGroupId;

import java.util.List;

@Repository
public interface UserHasGroupRepository extends JpaRepository<UserHasGroup, UserGroupId> {

    @Query("SELECT uhg FROM UserHasGroup uhg JOIN FETCH uhg.group g " +
            "WHERE uhg.userId = ?1")
    List<UserHasGroup> findByUserId(String userId);
}
