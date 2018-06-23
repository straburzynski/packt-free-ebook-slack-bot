package pl.straburzynski.packt.ebook.service;

import org.quartz.SchedulerException;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.model.scheduler.JobDescriptor;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface SchedulerJobService {

    JobDescriptor createJob(JobDescriptor descriptor);

    JobDescriptor createJob(Job job);

    void stopAllJobs();

    Optional<JobDescriptor> findJob(String name);

    Optional<List<String>> findAllJobs() throws SchedulerException, ParseException;

    void deleteJob(String name);

    Optional<List<String>> startAllActivePacktJobs() throws SchedulerException, ParseException;

    Optional<List<String>> restartAllActivePacktJobs() throws SchedulerException, ParseException;

}
