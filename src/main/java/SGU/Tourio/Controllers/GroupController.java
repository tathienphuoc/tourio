package SGU.Tourio.Controllers;

import SGU.Tourio.DTO.CreateGroupDTO;
import SGU.Tourio.DTO.UpdateGroupDTO;
import SGU.Tourio.Models.Customer;
import SGU.Tourio.Models.Group;
import SGU.Tourio.Services.*;
import SGU.Tourio.lib.m2m.GroupCostMapper;
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
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    CostTypeService costTypeService;

    @Autowired
    GroupEmpMapper groupEmpMapper;

    @Autowired
    GroupCostMapper groupCostMapper;

    @Autowired
    TourService tourService;

    @GetMapping("/group")
    public String index(Model model, @RequestParam("from") Optional<String> from, @RequestParam("to") Optional<String> to) throws ParseException {
        model.addAttribute("groups", groupService.getAllForView(from, to));
        return "Group/index";
    }

    @GetMapping("/group/report")
    public String report(Model model, @RequestParam("from") Optional<String> from, @RequestParam("to") Optional<String> to) throws ParseException {
        model.addAttribute("groups", groupService.getForReport(from, to));
        return "Group/report";
    }

    @GetMapping("/group/delete/{id}")
    public String deleteGroup(@PathVariable(value = "id") long id) {
        groupService.delete(id);
        return "redirect:/group";
    }

    @GetMapping("/group/create")
    public String createGroup(Model model) {
        model.addAttribute("group", new CreateGroupDTO());
        setupData(model);
        return "Group/create";
    }

    @PostMapping("/group/create")
    public String createGroup(Model model, @ModelAttribute("group") CreateGroupDTO dto) {
        try {
            groupService.create(dto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("group", dto);
            setupData(model);
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
        dto.setCostData(groupCostMapper.toJsonString(group.getGroupCostRels()));

        model.addAttribute("group", dto);
        setupData(model);
        return "Group/update";
    }

    @PostMapping("/group/update")
    public String updateGroup(Model model, HttpServletRequest request, @ModelAttribute("group") UpdateGroupDTO dto) {
        try {
            groupService.update(dto);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            model.addAttribute("error", e.getMessage());
            setupData(model);
            return "Group/update";
        }
        return "redirect:/group";
    }

    private void setupData(Model model) {
        model.addAttribute("tours", tourService.getAll());
        model.addAttribute("customers", customerService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("jobs", jobService.getAll());
        model.addAttribute("costTypes", costTypeService.getAll());
    }
}
