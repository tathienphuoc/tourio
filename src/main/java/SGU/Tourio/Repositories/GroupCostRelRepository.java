package SGU.Tourio.Repositories;

import SGU.Tourio.Models.GroupCostRel;
import SGU.Tourio.Models.GroupCostRelID;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Models.GroupEmployeeRelID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupCostRelRepository extends JpaRepository<GroupCostRel, GroupCostRelID> {

}