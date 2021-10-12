package SGU.Tourio.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SGU.Tourio.Models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}