package SGU.Tourio.Controllers;

import SGU.Tourio.DTO.CreateCostTypeDTO;
import SGU.Tourio.Models.CostType;
import SGU.Tourio.Services.CostTypeService;
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
public class CostTypeController {
    @Autowired
    CostTypeService costTypeService;
    
    @GetMapping("/cost-type")
    public String index(Model model) {
        model.addAttribute("costTypes", costTypeService.getAll());
        return "CostType/index";
    }

    @GetMapping("/cost-type/delete/{id}")
    public String deleteCostType(@PathVariable(value = "id") long id) {
        costTypeService.delete(id);
        return "redirect:/cost-type";
    }

    @GetMapping("/cost-type/create")
    public String createCostType(Model model) {
        model.addAttribute("costType", new  CreateCostTypeDTO());
        return "CostType/create";
    }

    @PostMapping("/cost-type/create")
    public String createCostType(Model model,@ModelAttribute("costType") CreateCostTypeDTO dto) {
        try {
            costTypeService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "CostType/create";
        }
        return "redirect:/cost-type";
    }

    @GetMapping("/cost-type/update/{id}")
    public String updateCostType(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("costType", costTypeService.get(id));
        return "CostType/update";
    }

    @PostMapping("/cost-type/update")
    public String updateCostType(Model model, @ModelAttribute("costType") CostType costType) {
        try {
            costTypeService.update(costType);
        } catch (EntityExistsException | NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "CostType/update";
        }
        return "redirect:/cost-type";
    }
}
