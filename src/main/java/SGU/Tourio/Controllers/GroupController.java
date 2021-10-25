package SGU.Tourio.Controllers;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.DTO.UpdateGroupDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Services.CustomerService;
import SGU.Tourio.Services.EmployeeService;
import SGU.Tourio.Services.GroupService;
import SGU.Tourio.Services.JobService;
import SGU.Tourio.lib.m2m.GroupEmpMapper;
import javassist.NotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    JobService jobService;

    @Autowired
    GroupEmpMapper groupEmpMapper;

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
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("jobs", jobService.getAll());
        return "Group/create";
    }

    @PostMapping("/group/create")
    public String createGroup(Model model, @ModelAttribute("group") CreateGroupDTO dto) {
        try {
            groupService.create(dto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("group", dto);
            model.addAttribute("customers", customerService.getAll());
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("jobs", jobService.getAll());
            return "Group/create";
        }
        return "redirect:/group";
    }

    @GetMapping("/group/update/{id}")
    public String updateGroup(Model model, @PathVariable(value = "id") long id) throws Exception {
        Group group = groupService.get(id);
        UpdateGroupDTO dto = new ModelMapper().map(group, UpdateGroupDTO.class);

        List<Long> customerIds = group.getCustomers().stream().map(Customer::getId).collect(Collectors.toList());
        dto.setCustomerIds(customerIds);
        dto.setEmployeeData(groupEmpMapper.toJsonString(group.getGroupEmployeeRels()));

        model.addAttribute("group", dto);
        model.addAttribute("customers", customerService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("jobs", jobService.getAll());
        return "Group/update";
    }

    @PostMapping("/group/update")
    public String updateGroup(Model model, HttpServletRequest request, @ModelAttribute("group") UpdateGroupDTO dto) {
        try {
            groupService.update(dto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
//            model.addAttribute("group", dto);
            model.addAttribute("customers", customerService.getAll());
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("jobs", jobService.getAll());
            return "Group/update";
//            String referer = request.getHeader("Referer");
//            return "redirect:"+ referer;
        }
        return "redirect:/group";
    }
}
