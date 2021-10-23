package SGU.Tourio.Controllers;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.DTO.UpdateGroupDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Services.CustomerService;
import SGU.Tourio.Services.EmployeeService;
import SGU.Tourio.Services.GroupService;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/group")
    public String index(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "Group/index";
    }

    @GetMapping("/group/delete/{id}")
    public String deleteGroup(@PathVariable(value = "id") long id) {
        groupService.delete(id);
        return "redirect:/group";
    }

    @GetMapping("/group/create")
    public String createGroup(Model model) {
        model.addAttribute("group", new CreateGroupDTO());
        model.addAttribute("customers", customerService.getAll());
        return "Group/create";
    }

    @PostMapping("/group/create")
    public String createGroup(Model model, @ModelAttribute("group") CreateGroupDTO dto) {
        try {
            groupService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("entityExistsException", e.getMessage());
            return "Group/create";
        }
        return "redirect:/group";
    }

    @GetMapping("/group/update/{id}")
    public String updateGroup(Model model, @PathVariable(value = "id") long id) {
        Group group = groupService.get(id);
        UpdateGroupDTO dto = new ModelMapper().map(group, UpdateGroupDTO.class);
        List<Long> customerIds = group.getCustomers().stream().map(Customer::getId).collect(Collectors.toList());
        dto.setCustomerIds(customerIds);
        model.addAttribute("group", dto);
        model.addAttribute("customers", customerService.getAll());
        return "Group/update";
    }

    @PostMapping("/group/update")
    public String updateGroup(Model model, @ModelAttribute("group") UpdateGroupDTO group) {
        try {
            groupService.update(group);
        } catch (EntityExistsException | NotFoundException e) {
            model.addAttribute("entityExistsException", e.getMessage());
            return "Group/update";
        }
        return "redirect:/group";
    }
}
