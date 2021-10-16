package SGU.Tourio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import SGU.Tourio.Models.Employee;
import SGU.Tourio.Models.Job;
import SGU.Tourio.Models.Location;
import SGU.Tourio.Services.EmployeeService;
import SGU.Tourio.Services.JobService;
import SGU.Tourio.Services.LocationService;
import javassist.NotFoundException;

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
    public String homePage() {
        return "blank";
    }

    // @GetMapping("/locations")
    // public String location(Model model) {
    //     model.addAttribute("locations", locationService.getAll());
    //     return "locations";
    // }

    // @GetMapping("/deleteLocation/{id}")
    // public String deleteLocation(Model model, @PathVariable(value = "id") long id) {
    //     locationService.delete(id);
    //     return "redirect:/locations";
    // }

    @GetMapping("/deleteJob/{id}")
    public String deleteJob(Model model,@PathVariable(value = "id") long id) {
        jobService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/updateJob/{id}")
    public String updateJob(Model model, @PathVariable(value = "id") long id) {
        Job job = jobService.get(id);
        job.setName("updated");
        try {
            jobService.update(job);
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/";
    }

    public void temporaryData() {
        locationService.create(new Location((long) 1, "HaNoi"));
        locationService.create(new Location((long) 2, "TPHCM"));
        locationService.create(new Location((long) 3, "DaNang"));

        jobService.create(new Job((long) 1, "Job 1"));
        jobService.create(new Job((long) 2, "Job 2"));
        jobService.create(new Job((long) 3, "Job 3"));

        employeeService.create(new Employee((long) 1, "Employee 1"));
        employeeService.create(new Employee((long) 2, "Employee 1"));
        employeeService.create(new Employee((long) 3, "Employee 1"));
    }

}
