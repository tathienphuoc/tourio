package SGU.Tourio.Controllers;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import SGU.Tourio.DTO.CreateEmployeeDTO;
import SGU.Tourio.Models.Employee;
import SGU.Tourio.Services.EmployeeService;

import java.text.ParseException;
import java.util.Optional;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/employee")
    public String index(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        return "Employee/index";
    }

    @GetMapping("/employee/report")
    public String report(Model model, @RequestParam("from") Optional<String> from, @RequestParam("to") Optional<String> to) throws ParseException {
        model.addAttribute("employees", employeeService.getForTourReport(from, to));
        return "Employee/report";
    }

    @GetMapping("/employee/delete/{id}")
    public String deleteEmployee(Model model, @PathVariable(value = "id") long id) {
        employeeService.delete(id);
        return "redirect:/employee";
    }

    @GetMapping("/employee/create")
    public String createEmployee(Model model) {
        model.addAttribute("employee", new CreateEmployeeDTO());
        return "Employee/create";
    }

    @PostMapping("/employee/create")
    public String createEmployee(Model model,@ModelAttribute("employee") CreateEmployeeDTO dto) {
        try {
            employeeService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Employee/create";
        }
        return "redirect:/employee";
    }

    @GetMapping("/employee/update/{id}")
    public String updateEmployee(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("employee", employeeService.get(id));
        return "Employee/update";
    }

    @PostMapping("/employee/update")
    public String updateEmployee(Model model, @ModelAttribute("employee") Employee employee) {
        try {
            employeeService.update(employee);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Employee/update";
        }
        return "redirect:/employee";
    }
}
