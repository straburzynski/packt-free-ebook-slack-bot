package pl.straburzynski.packt.ebook.service;

import pl.straburzynski.packt.ebook.model.Job;

import java.util.List;
import java.util.Optional;

public interface JobService {

    List<Job> getAllJobs();
    List<Job> getAllActiveJobs();
    Optional<Job> findById(Long id);
    Optional<Job> findByJobName(String jobName);
    Job saveJob(Job job);
    void deleteJob(Long id);
}
