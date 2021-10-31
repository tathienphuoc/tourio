package SGU.Tourio.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateEmployeeDTO;
import SGU.Tourio.DTO.ReportEmployeeDTO;
import SGU.Tourio.Models.Employee;
import SGU.Tourio.Models.GroupEmployeeRel;
import SGU.Tourio.Repositories.EmployeeRepository;
import SGU.Tourio.Repositories.GroupEmpRelRepository;
import SGU.Tourio.Utils.FormatString;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    GroupEmpRelRepository groupRepository;

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public List<ReportEmployeeDTO> getForTourReport(Optional<String> from, Optional<String> to) throws ParseException {
        List<Employee> employees = getAll();
        List<GroupEmployeeRel> groups;
        if (from.isPresent() && to.isPresent()) {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from.get());
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to.get());
            groups = groupRepository.findAllByGroupDateStartBetween(fromDate, toDate);
        } else {
            groups = groupRepository.findAll();
        }

        List<ReportEmployeeDTO> dtoList = new ArrayList<>();
        for (Employee employee : employees) {
            ReportEmployeeDTO dto = new ModelMapper().map(employee, ReportEmployeeDTO.class);
            List<GroupEmployeeRel> groupEmployeeRel = groups.stream()
                    .filter(g -> Objects.equals(g.getEmployee().getId(), employee.getId()))
                    .collect(Collectors.toList());
            dto.setGroupCount(groupEmployeeRel.size());
            dto.setJobs(String.join(", ", groupEmployeeRel.stream().map(g -> g.getJob().getName()).collect(Collectors.toList())));
            dtoList.add(dto);
        }
        return dtoList;
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
