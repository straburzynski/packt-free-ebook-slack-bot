package pl.straburzynski.packt.ebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.straburzynski.packt.ebook.exception.JobNotFoundException;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.repository.JobRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private final ValidationService validationService;
    private final SchedulerJobService schedulerJobService;
    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository,
                          @Lazy SchedulerJobService schedulerJobService,
                          @Lazy ValidationService validationService) {
        this.jobRepository = jobRepository;
        this.schedulerJobService = schedulerJobService;
        this.validationService = validationService;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public List<Job> getAllActiveJobs() {
        return jobRepository.findAllByActiveIsTrue();
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
        validationService.validate(job);
        job.setCreatedDate(LocalDateTime.now());
        Job savedJob = jobRepository.save(job);
        schedulerJobService.createJob(job);
        return savedJob;
    }

    @Override
    public void deleteJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        jobRepository.delete(job.orElseThrow(() -> new JobNotFoundException("Job not found")));
    }

}
