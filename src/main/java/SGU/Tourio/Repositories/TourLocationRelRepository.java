package SGU.Tourio.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SGU.Tourio.Models.TourLocationRel;
import SGU.Tourio.Models.TourLocationRelID;

@Repository
public interface TourLocationRelRepository extends JpaRepository<TourLocationRel, TourLocationRelID> {
    // List<TourLocationRel> findAllByGroupDateStartBetween(Date dateStart, Date dateEnd);

}