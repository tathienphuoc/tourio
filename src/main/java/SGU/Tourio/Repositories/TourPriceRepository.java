package SGU.Tourio.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SGU.Tourio.Models.TourPrice;

@Repository
public interface TourPriceRepository extends JpaRepository<TourPrice, Long> {

}