package pl.straburzynski.packt.ebook.converter;

import org.junit.Test;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.model.scheduler.JobDescriptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JobConverterTest {

    private JobConverter jobConverter = new JobConverter();

    private Job job1 = Job.builder()
            .id(1L)
            .jobName("Job name 1")
            .botName("Bot name 1")
            .channel("Channel 1")
            .webhook("http://test.com/x/y/z")
            .scheduler("* 30 13 ? * * *")
            .active(true)
            .createdDate(LocalDateTime.now())
            .build();

    private Job job2 = Job.builder()
            .id(2L)
            .jobName("Job name 2")
            .botName("Bot name 2")
            .channel("Channel 2")
            .webhook("http://test.com/x/y/z")
            .scheduler("* 30 13 ? * * *")
            .active(true)
            .createdDate(LocalDateTime.now())
            .build();


    @Test
    public void fromJob() {
        JobDescriptor jobDescriptor1 = jobConverter.fromJob(job1);
        assertThat(jobDescriptor1.getName()).isEqualTo(job1.getJobName());
        assertThat(jobDescriptor1.getJobId()).isEqualTo(String.valueOf(job1.getId()));
        assertThat(jobDescriptor1.getWebhook()).isEqualTo(job1.getWebhook());
        assertThat(jobDescriptor1.getChrono()).isEqualTo(job1.getScheduler());
        assertThat(jobDescriptor1.getGroup()).isEqualTo("PacktGroup");
    }


    @Test
    public void fromJobList() {
        List<Job> jobList = Arrays.asList(job1, job2);
        List<JobDescriptor> jobDescriptorList = jobConverter.fromJobList(jobList);
        assertThat(jobDescriptorList.size()).isEqualTo(2);
        assertThat(jobDescriptorList.get(0).getName()).isEqualTo(job1.getJobName());
        assertThat(jobDescriptorList.get(1).getName()).isEqualTo(job2.getJobName());
    }
}