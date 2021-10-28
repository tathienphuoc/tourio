package SGU.Tourio.Repositories;

import SGU.Tourio.Models.Group;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Models.GroupEmployeeRelID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GroupEmpRelRepository extends JpaRepository<GroupEmployeeRel, GroupEmployeeRelID> {
    List<GroupEmployeeRel> findAllByGroupDateStartBetween(Date dateStart, Date dateEnd);

}