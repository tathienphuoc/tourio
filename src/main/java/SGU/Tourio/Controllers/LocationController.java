package SGU.Tourio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import SGU.Tourio.Models.Location;
import SGU.Tourio.Services.LocationService;
import javassist.NotFoundException;

@Controller
// @RequestMapping("/locations")
public class LocationController {
    @Autowired
    LocationService locationService;
    
    @GetMapping("/locations")
    public String location(Model model) {
        // temporaryData();
        model.addAttribute("locations", locationService.getAll());
        return "Location/index";
    }

    @GetMapping("/locations/delete/{id}")
    public String deleteLocation(Model model, @PathVariable(value = "id") long id) {
        locationService.delete(id);
        return "redirect:/locations";
    }

    @GetMapping("/locations/create")
    public String createLocation(Model model) {
        model.addAttribute("location", new Location());
        return "Location/create";
    }

    @PostMapping("/locations/create")
    public String createLocation(@ModelAttribute("location") Location location) {
        locationService.create(location);
        return "redirect:/locations";
    }

    @GetMapping("/locations/update/{id}")
    public String updateLocation(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("location", locationService.get(id));
        return "Location/update";
    }

    @PostMapping("/locations/update")
    public String updateLocation(@ModelAttribute("location") Location location) {
        try {
            locationService.update(location);
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/locations";
    }

    public void temporaryData() {
        locationService.create(new Location((long) 1, "HaNoi"));
        locationService.create(new Location((long) 2, "TPHCM"));
        locationService.create(new Location((long) 3, "DaNang"));
        locationService.create(new Location((long) 4, "VungTau"));
        locationService.create(new Location((long) 5, "DaLat"));
        locationService.create(new Location((long) 6, "NhaTrang"));

        // jobService.create(new Job((long) 1, "Job 1"));
        // jobService.create(new Job((long) 2, "Job 2"));
        // jobService.create(new Job((long) 3, "Job 3"));

        // employeeService.create(new Employee((long) 1, "Employee 1"));
        // employeeService.create(new Employee((long) 2, "Employee 1"));
        // employeeService.create(new Employee((long) 3, "Employee 1"));
    }
}
