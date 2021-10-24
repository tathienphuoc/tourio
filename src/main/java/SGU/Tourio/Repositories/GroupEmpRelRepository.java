package SGU.Tourio.Repositories;

import SGU.Tourio.Models.Group;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Models.GroupEmployeeRelID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupEmpRelRepository extends JpaRepository<GroupEmployeeRel, GroupEmployeeRelID> {

}