package pl.straburzynski.packt.ebook.service;

import org.quartz.SchedulerException;
import pl.straburzynski.packt.ebook.model.Job;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface JobService {

    List<Job> getAllJobs();
    List<Job> getAllActiveJobs();
    Optional<Job> findById(Long id);
    Optional<Job> findByJobName(String jobName);
    Job saveJob(Job job);
    Job editJob(Job job) throws ParseException, SchedulerException;
    void deleteJob(Long id) throws ParseException, SchedulerException;
}
