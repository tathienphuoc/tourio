package SGU.Tourio.Repositories;

import SGU.Tourio.Models.CostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostTypeRepository extends JpaRepository<CostType, Long> {

}