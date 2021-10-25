package SGU.Tourio.Controllers;

import SGU.Tourio.DTO.CreateTourTypeDTO;
import SGU.Tourio.Models.TourType;
import SGU.Tourio.Services.TourTypeService;
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
public class TourTypeController {
    @Autowired
    TourTypeService tourTypeService;
    
    @GetMapping("/tour-type")
    public String index(Model model) {
        model.addAttribute("tourTypes", tourTypeService.getAll());
        return "TourType/index";
    }

    @GetMapping("/tour-type/delete/{id}")
    public String deleteTourType(@PathVariable(value = "id") long id) {
        tourTypeService.delete(id);
        return "redirect:/tour-type";
    }

    @GetMapping("/tour-type/create")
    public String createTourType(Model model) {
        model.addAttribute("tourType", new  CreateTourTypeDTO());
        return "TourType/create";
    }

    @PostMapping("/tour-type/create")
    public String createTourType(Model model,@ModelAttribute("tourType") CreateTourTypeDTO dto) {
        try {
            tourTypeService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "TourType/create";
        }
        return "redirect:/tour-type";
    }

    @GetMapping("/tour-type/update/{id}")
    public String updateTourType(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("tourType", tourTypeService.get(id));
        return "TourType/update";
    }

    @PostMapping("/tour-type/update")
    public String updateTourType(Model model, @ModelAttribute("tourType") TourType tourType) {
        try {
            tourTypeService.update(tourType);
        } catch (EntityExistsException | NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "TourType/update";
        }
        return "redirect:/tour-type";
    }
}
