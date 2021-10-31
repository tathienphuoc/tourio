package SGU.Tourio.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SGU.Tourio.Models.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // Tour merge(Tour tour);
    // List<Tour> findAllByDateStartBetween(Date dateStart, Date dateEnd);
}