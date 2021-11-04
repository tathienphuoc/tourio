package SGU.Tourio.Repositories;

import SGU.Tourio.Models.Group;
import SGU.Tourio.Models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByCreatedAtBetween(Date dateStart, Date dateEnd);

    List<Group> findAllByTourAndCreatedAtBetween(Tour tour, Date dateStart, Date dateEnd);

    List<Group> findAllByTour(Tour tour);

}