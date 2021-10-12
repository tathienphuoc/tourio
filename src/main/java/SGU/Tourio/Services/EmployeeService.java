package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.Models.Employee;
import SGU.Tourio.Repositories.EmployeeRepository;
import javassist.NotFoundException;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee get(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.isPresent() ? employee.get() : null;
    }

    public Employee create(Employee employee) throws EntityExistsException {
        Employee isExist = get(employee.getId());
        if (isExist != null) {
            throw new EntityExistsException("Existed");
        }
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) throws NotFoundException {
        Employee isExist = get(employee.getId());
        if (isExist == null) {
            throw new NotFoundException("Not Existed");
        }
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        Employee employee = get(id);
        if (employee != null)
            employeeRepository.delete(employee);
    }
}
