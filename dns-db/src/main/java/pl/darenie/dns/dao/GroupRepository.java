package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
