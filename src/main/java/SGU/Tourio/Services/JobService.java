package SGU.Tourio.Services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SGU.Tourio.Models.Job;
import SGU.Tourio.Repositories.JobRepository;
import javassist.NotFoundException;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public Job get(long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.orElse(null);
    }

    public Job create(Job job) throws EntityExistsException {
        Job isExist = get(job.getId());
        if (isExist != null) {
            throw new EntityExistsException("Existed");
        }
        return jobRepository.save(job);
    }

    public Job update(Job job) throws NotFoundException {
        Job isExist = get(job.getId());
        if (isExist == null) {
            throw new NotFoundException("Not Existed");
        }
        return jobRepository.save(job);
    }

    public void delete(Long id) {
        Job job = get(id);
        if (job != null)
            jobRepository.delete(job);
    }
}
