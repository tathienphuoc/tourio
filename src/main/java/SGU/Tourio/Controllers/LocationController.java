package SGU.Tourio.Controllers;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import SGU.Tourio.DTO.CreateLocationDTO;
import SGU.Tourio.Models.Location;
import SGU.Tourio.Services.LocationService;

@Controller
// @RequestMapping("/location")
public class LocationController {
    @Autowired
    LocationService locationService;
    
    @GetMapping("/location")
    public String index(Model model) {
        model.addAttribute("locations", locationService.getAll());
        return "Location/index";
    }

    @GetMapping("/location/delete/{id}")
    public String deleteLocation(Model model, @PathVariable(value = "id") long id) {
        locationService.delete(id);
        return "redirect:/location";
    }

    @GetMapping("/location/create")
    public String createLocation(Model model) {
        model.addAttribute("location", new  CreateLocationDTO());
        return "Location/create";
    }

    @PostMapping("/location/create")
    public String createLocation(Model model,@ModelAttribute("location") CreateLocationDTO dto) {
        try {
            locationService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Location/create";
        }
        return "redirect:/location";
    }

    @GetMapping("/location/update/{id}")
    public String updateLocation(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("location", locationService.get(id));
        return "Location/update";
    }

    @PostMapping("/location/update")
    public String updateLocation(Model model, @ModelAttribute("location") Location location) {
        try {
            locationService.update(location);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Location/update";
        }
        return "redirect:/location";
    }
}
