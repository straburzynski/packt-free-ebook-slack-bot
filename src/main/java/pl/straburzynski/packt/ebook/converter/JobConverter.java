package pl.straburzynski.packt.ebook.converter;

import org.springframework.stereotype.Component;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.model.scheduler.JobDescriptor;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobConverter {

    public JobDescriptor fromJob(Job job) {
        return JobDescriptor.builder()
                .name(job.getJobName())
                .jobId(String.valueOf(job.getId()))
                .webhook(job.getWebhook())
                .chrono(job.getScheduler())
                .group("PacktGroup")
                .build();
    }

    public List<JobDescriptor> fromJobList(List<Job> jobList) {
        List<JobDescriptor> jobDescriptorList = new ArrayList<>();
        for (Job job : jobList) {
            JobDescriptor JobDescriptor = fromJob(job);
            jobDescriptorList.add(JobDescriptor);
        }
        return jobDescriptorList;
    }

}
