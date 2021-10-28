package SGU.Tourio.Repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import SGU.Tourio.Models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SGU.Tourio.Models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByName(String name);

    Optional<Employee> findByName(String name);

}