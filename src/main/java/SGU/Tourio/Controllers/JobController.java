package SGU.Tourio.Controllers;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import SGU.Tourio.DTO.CreateJobDTO;
import SGU.Tourio.Models.Job;
import SGU.Tourio.Services.JobService;

@Controller
public class JobController {
    @Autowired
    JobService jobService;
    
    @GetMapping("/job")
    public String index(Model model) {
        model.addAttribute("jobs", jobService.getAll());
        return "Job/index";
    }

    @GetMapping("/job/delete/{id}")
    public String deleteJob(Model model, @PathVariable(value = "id") long id) {
        jobService.delete(id);
        return "redirect:/job";
    }

    @GetMapping("/job/create")
    public String createJob(Model model) {
        model.addAttribute("job", new  CreateJobDTO());
        return "Job/create";
    }

    @PostMapping("/job/create")
    public String createJob(Model model,@ModelAttribute("job") CreateJobDTO dto) {
        try {
            jobService.create(dto);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Job/create";
        }
        return "redirect:/job";
    }

    @GetMapping("/job/update/{id}")
    public String updateJob(Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("job", jobService.get(id));
        return "Job/update";
    }

    @PostMapping("/job/update")
    public String updateJob(Model model, @ModelAttribute("job") Job job) {
        try {
            jobService.update(job);
        } catch (EntityExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "Job/update";
        }
        return "redirect:/job";
    }
}
