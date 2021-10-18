package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateEmployeeDTO;
import SGU.Tourio.Models.Employee;
import SGU.Tourio.Repositories.EmployeeRepository;
import SGU.Tourio.Utils.FormatString;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee get(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        Optional<Employee> employee = employeeRepository.findById(id);

        return employee.orElse(null);
    }

    public Employee get(String name) {
        if (name == null)
            throw new NullPointerException("Name is null");

        // Format
        name = FormatString.TitleCase(name);

        Optional<Employee> employee = employeeRepository.findByName(name);
        return employee.orElse(null);
    }

    public Employee create(CreateEmployeeDTO dto) throws EntityExistsException {
        // Format
        dto.setName(FormatString.TitleCase(dto.getName()));

        if (employeeRepository.existsByName(dto.getName())) {
            throw new EntityExistsException(dto.getName() + " existed");
        }
        Employee employee = new ModelMapper().map(dto, Employee.class);
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) throws EntityExistsException {
        // Format
        employee.setName(FormatString.TitleCase(employee.getName()));
        Employee isExist = get(employee.getName());
        if (isExist != null && !employee.getId().equals(isExist.getId())) {
            throw new EntityExistsException(employee.getName() + " existed");
        }
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        if (employeeRepository.existsById(id))
            employeeRepository.deleteById(id);
    }
}
