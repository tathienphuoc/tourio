package SGU.Tourio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import SGU.Tourio.Services.EmployeeService;
import SGU.Tourio.Services.JobService;
import SGU.Tourio.Services.LocationService;

@Controller
public class SimpleController {
    @Autowired
    LocationService locationService;
    @Autowired
    JobService jobService;
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/")
    public String homePage(Model model) {
        // temporaryData();
        model.addAttribute("locations", locationService.getAll());
        model.addAttribute("jobs", jobService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        return "home";
    }

    @GetMapping("/blank")
    public String blank() {
        return "blank";
    }

    @GetMapping("/template")
    public String template() {
        return "template";
    }
    
    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
