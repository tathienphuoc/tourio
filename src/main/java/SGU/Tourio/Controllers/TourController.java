package SGU.Tourio.Controllers;

import java.text.ParseException;
import java.util.Optional;

import SGU.Tourio.lib.m2m.TourPriceMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import SGU.Tourio.DTO.CreateTourDTO;
import SGU.Tourio.DTO.UpdateTourDTO;
import SGU.Tourio.Models.Tour;
import SGU.Tourio.Services.LocationService;
import SGU.Tourio.Services.TourService;
import SGU.Tourio.Services.TourTypeService;
import SGU.Tourio.lib.m2m.TourLocationMapper;

@Controller
public class TourController {
    @Autowired
    TourService tourService;

    @Autowired
    LocationService locationService;

    @Autowired
    TourTypeService tourTypeService;

    @Autowired
    TourLocationMapper tourLocationMapper;

    @Autowired
    TourPriceMapper tourPriceMapper;

    @GetMapping("/tour")
    public String index(Model model) throws ParseException {
        model.addAttribute("tours", tourService.getAll());
        return "Tour/index";
    }

    @GetMapping("/tour/report")
    public String index(Model model, @RequestParam("from") Optional<String> from,
                        @RequestParam("to") Optional<String> to) throws ParseException {
        model.addAttribute("tours", tourService.getForSaleReport(from, to));
        return "Tour/report";
    }

    @GetMapping("/tour/delete/{id}")
    public String deleteGroup(@PathVariable(value = "id") long id) {
        tourService.delete(id);
        return "redirect:/tour";
    }

    @GetMapping("/tour/create")
    public String createTour(Model model) {
        model.addAttribute("tour", new CreateTourDTO());
        model.addAttribute("locations", locationService.getAll());
        model.addAttribute("tourTypes", tourTypeService.getAll());
        return "Tour/create";
    }

    @PostMapping("/tour/create")
    public String createTour(Model model, @ModelAttribute("tour") CreateTourDTO dto) {
        try {
            // System.out.println("Controlelr "+ dto);
            // System.out.println("\n\n\n\n\n\n\n");
            tourService.create(dto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tour", dto);
            model.addAttribute("locations", locationService.getAll());
            model.addAttribute("tourTypes", tourTypeService.getAll());
            return "Tour/create";
        }
        return "redirect:/tour";
    }

    @GetMapping("/tour/update/{id}")
    public String updateGroup(Model model, @PathVariable(value = "id") long id) throws Exception {
        Tour tour = tourService.get(id);
        UpdateTourDTO dto = new ModelMapper().map(tour, UpdateTourDTO.class);

        dto.setLocationData(tourLocationMapper.toJsonString(tour.getTourLocationRels()));
        dto.setTourPriceData(tourPriceMapper.toJsonString(tour.getTourPrices()));
        model.addAttribute("tour", dto);
        model.addAttribute("locations", locationService.getAll());
        model.addAttribute("tourTypes", tourTypeService.getAll());

        return "Tour/update";
    }

    @PostMapping("/tour/update")
    public String updateTour(Model model, @ModelAttribute("tour") UpdateTourDTO dto) {
        try {
            System.out.println(dto);
            tourService.update(dto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tour", dto);
            model.addAttribute("locations", locationService.getAll());
            model.addAttribute("tourTypes", tourTypeService.getAll());
            return "Tour/update";
        }
        return "redirect:/tour";
    }
}
