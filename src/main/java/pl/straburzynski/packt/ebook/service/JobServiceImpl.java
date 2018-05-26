package pl.straburzynski.packt.ebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.straburzynski.packt.ebook.exception.JobNotFoundException;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public Optional<Job> findByJobName(String jobName) {
        return jobRepository.findByJobName(jobName);
    }

    @Override
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        jobRepository.delete(job.orElseThrow(
                () -> new JobNotFoundException("Job not found"))
        );
    }

}
