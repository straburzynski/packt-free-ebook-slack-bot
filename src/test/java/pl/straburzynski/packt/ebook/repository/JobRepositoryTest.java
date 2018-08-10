package pl.straburzynski.packt.ebook.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.straburzynski.packt.ebook.model.Job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    private Job job1 = Job.builder()
            .jobName("Job name 1")
            .botName("Bot name 1")
            .channel("Channel 1")
            .webhook("http://test.com/x/y/z")
            .scheduler("* 30 13 ? * * *")
            .active(true)
            .createdDate(LocalDateTime.now())
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now()).build();

    private Job job2 = Job.builder()
            .jobName("Job name 2")
            .botName("Bot name 2")
            .channel("Channel 2")
            .webhook("http://test.com/x/y/z")
            .scheduler("* 30 13 ? * * *")
            .active(false)
            .createdDate(LocalDateTime.now())
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now()).build();

    @Before
    public void setUp() {
        jobRepository.save(job1);
        jobRepository.save(job2);
    }

    @Test
    public void findByJobName_jobExists() {
        Optional<Job> job = jobRepository.findByJobName(job1.getJobName());
        assertThat(job.get().getId()).isNotNull();
        assertThat(job.get().getJobName()).isEqualTo(job1.getJobName());
        assertThat(job.get().getBotName()).isEqualTo(job1.getBotName());
        assertThat(job.get().getChannel()).isEqualTo(job1.getChannel());
        assertThat(job.get().getWebhook()).isEqualTo(job1.getWebhook());
        assertThat(job.get().getScheduler()).isEqualTo(job1.getScheduler());
    }

    @Test
    public void findByJobName_jobDoesNotExists() {
        String jobName = "Test";
        Optional<Job> job = jobRepository.findByJobName(jobName);
        assertThat(job).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllByActiveIsTrue() {
        List<Job> activeJobs = jobRepository.findAllByActiveIsTrue();
        assertThat(activeJobs.size()).isEqualTo(1);
    }
}
