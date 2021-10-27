package SGU.Tourio.Repositories;

import SGU.Tourio.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByDateStartBetween(Date dateStart, Date dateEnd);
}