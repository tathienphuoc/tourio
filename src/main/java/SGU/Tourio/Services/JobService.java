package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.DTO.CreateJobDTO;
import SGU.Tourio.Models.Job;
import SGU.Tourio.Repositories.JobRepository;
import SGU.Tourio.Utils.FormatString;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public Job get(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        Optional<Job> job = jobRepository.findById(id);

        return job.orElse(null);
    }

    public Job get(String name) {
        if (name == null)
            throw new NullPointerException("Name is null");

        // Format
        name = FormatString.TitleCase(name);

        Optional<Job> job = jobRepository.findByName(name);
        return job.orElse(null);
    }

    public Job create(CreateJobDTO dto) throws EntityExistsException {
        // Format
        dto.setName(FormatString.TitleCase(dto.getName()));

        if (jobRepository.existsByName(dto.getName())) {
            throw new EntityExistsException(dto.getName() + " existed");
        }
        Job job = new ModelMapper().map(dto, Job.class);
        return jobRepository.save(job);
    }

    public Job update(Job job) throws EntityExistsException {
        // Format
        job.setName(FormatString.TitleCase(job.getName()));
        Job isExist = get(job.getName());
        if (isExist != null && !job.getId().equals(isExist.getId())) {
            throw new EntityExistsException(job.getName() + " existed");
        }
        return jobRepository.save(job);
    }

    public void delete(Long id) {
        if (id == null)
            throw new NullPointerException("ID is null");

        if (jobRepository.existsById(id))
            jobRepository.deleteById(id);
    }
}
