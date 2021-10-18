package SGU.Tourio.Controllers;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import SGU.Tourio.DTO.CreateEmployeeDTO;
import SGU.Tourio.Models.Employee;
import SGU.Tourio.Services.EmployeeService;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    
    @GetMapping("/employees")
    public String index(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        return "Employee/index";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(Model model, @PathVariable(value = "id") long id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }

    @GetMapping("/employees/create")
    public String createEmployee(Model model) {
        model.addAttribute("employee", new  CreateEmployeeDTO());
        return "Employee/create";
    }

    @PostMapping("/employees/create")
    public String createEmployee(Model model,@ModelAttribute("employee") CreateEmployeeDTO dto) {
        try {
            employeeService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("entityExistsException", e.getMessage());
            return "Employee/create";
        }
        return "redirect:/employees";
    }

    @GetMapping("/employees/update/{id}")
    public String updateEmployee(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("employee", employeeService.get(id));
        return "Employee/update";
    }

    @PostMapping("/employees/update")
    public String updateEmployee(Model model, @ModelAttribute("employee") Employee employee) {
        try {
            employeeService.update(employee);
        } catch (EntityExistsException e) {
            model.addAttribute("entityExistsException", e.getMessage());
            return "Employee/update";
        }
        return "redirect:/employees";
    }
}
