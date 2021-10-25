package SGU.Tourio.Controllers;

import SGU.Tourio.DTO.CreateCustomerDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Services.CustomerService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityExistsException;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/customer")
    public String index(Model model) {
        model.addAttribute("customers", customerService.getAll());
        return "Customer/index";
    }

    @GetMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable(value = "id") long id) {
        customerService.delete(id);
        return "redirect:/customer";
    }

    @GetMapping("/customer/create")
    public String createCustomer(Model model) {
        model.addAttribute("customer", new CreateCustomerDTO());
        return "Customer/create";
    }

    @PostMapping("/customer/create")
    public String createCustomer(Model model, @ModelAttribute("customer") CreateCustomerDTO dto) {
        try {
            customerService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Customer/create";
        }
        return "redirect:/customer";
    }

    @GetMapping("/customer/update/{id}")
    public String updateCustomer(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("customer", customerService.get(id));
        return "Customer/update";
    }

    @PostMapping("/customer/update")
    public String updateCustomer(Model model, @ModelAttribute("customer") Customer customer) {
        try {
            customerService.update(customer);
        } catch (EntityExistsException | NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "Customer/update";
        }
        return "redirect:/customer";
    }
}
