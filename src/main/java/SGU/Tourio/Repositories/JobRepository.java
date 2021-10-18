package SGU.Tourio.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SGU.Tourio.Models.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByName(String name);

    boolean existsByName(String name);

}