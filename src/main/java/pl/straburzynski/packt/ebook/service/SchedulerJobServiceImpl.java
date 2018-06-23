package pl.straburzynski.packt.ebook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.straburzynski.packt.ebook.converter.JobConverter;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.model.scheduler.JobDescriptor;

import java.util.*;

import static org.quartz.JobKey.jobKey;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerJobServiceImpl implements SchedulerJobService {

    private static final String GROUP_NAME = "PacktGroup";
    private final Scheduler scheduler;
    private final JobService jobService;
    private final JobConverter jobConverter;


    public JobDescriptor createJob(JobDescriptor descriptor) {
        descriptor.setChrono(descriptor.getChrono());
        descriptor.setGroup(GROUP_NAME);
        JobDetail jobDetail = descriptor.buildJobDetail(descriptor.getGroup());
        Set<Trigger> triggersForJob = descriptor.buildTriggers(descriptor.getName(), descriptor.getChrono());
        log.info("Save new job: {}", jobDetail.getKey());
        try {
            scheduler.scheduleJob(jobDetail, triggersForJob, false);
            log.info("Job - {} saved successfully", jobDetail.getKey());
        } catch (SchedulerException e) {
            log.error("Could not save job - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
        return descriptor;
    }

    @Override
    public JobDescriptor createJob(Job job) {
        return createJob(jobConverter.fromJob(job));
    }

    public void stopAllJobs() {
        log.info("Removing all jobs");
        try {
            scheduler.clear();
            log.info("All jobs cleared");
        } catch (SchedulerException e) {
            log.error("Could not stop all jobs due to error - {}", e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }

    @Transactional(readOnly = true)
    public Optional<JobDescriptor> findJob(String name) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey(name, GROUP_NAME));
            if (Objects.nonNull(jobDetail))
                return Optional.of(JobDescriptor.buildDescriptor(jobDetail));
        } catch (SchedulerException e) {
            log.error("Could not find job with key - {}.{} due to error - {}", GROUP_NAME, name, e.getLocalizedMessage());
        }
        log.warn("Could not find job with key - {}.{}", GROUP_NAME, name);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<List<String>> findAllJobs() throws SchedulerException {
        List<String> pushNotificationIds = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                Date nextFire = scheduler.getTriggersOfJob(jobKey).stream().findFirst().get().getNextFireTime();
                Date previousFire = scheduler.getTriggersOfJob(jobKey).stream().findFirst().get().getPreviousFireTime();
                String cronExpression = scheduler.getJobDetail(jobKey).getJobDataMap().getString("chrono");
                String jobId = scheduler.getJobDetail(jobKey).getJobDataMap().getString("jobId");
                pushNotificationIds.add("Job name: " + jobKey.getName() + "job id: " + jobId + ", chrono: "
                        + cronExpression + ", next: " + nextFire + ", previous: " + previousFire);
            }
        }
        return Optional.of(pushNotificationIds);
    }

    public void deleteJob(String name) {
        try {
            scheduler.deleteJob(jobKey(name, GROUP_NAME));
            log.info("Deleted job with key - {}.{}", GROUP_NAME, name);
        } catch (SchedulerException e) {
            log.error("Could not delete job with key - {}.{} due to error - {}", GROUP_NAME, name, e.getLocalizedMessage());
        }
    }

    @Override
    public Optional<List<String>> startAllActivePacktJobs() throws SchedulerException {
        List<Job> jobs = jobService.getAllActiveJobs();
        List<JobDescriptor> jobDescriptors = jobConverter.fromJobList(jobs);
        jobDescriptors.forEach(this::createJob);
        return findAllJobs();
    }

    @Override
    public Optional<List<String>> restartAllActivePacktJobs() throws SchedulerException {
        stopAllJobs();
        return startAllActivePacktJobs();
    }

}
