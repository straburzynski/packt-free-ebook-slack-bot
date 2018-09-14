package pl.straburzynski.packt.ebook.controller;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.straburzynski.packt.ebook.exception.JobNotFoundException;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.service.JobService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.straburzynski.packt.ebook.utils.ObjectConverter.asJsonString;

public class JobControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }

    @Test
    public void createJob() throws Exception {
        String job1Json = asJsonString(job1);
        when(jobService.saveJob(job1)).thenReturn(job1);
        mockMvc.perform(
                post("/jobs")
                        .characterEncoding(CharEncoding.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(job1Json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobName", is(job1.getJobName())))
                .andExpect(jsonPath("$.botName", is(job1.getBotName())))
                .andExpect(jsonPath("$.channel", is(job1.getChannel())))
                .andExpect(jsonPath("$.webhook", is(job1.getWebhook())))
                .andExpect(jsonPath("$.scheduler", is(job1.getScheduler())))
                .andExpect(jsonPath("$.active", is(job1.isActive())))
                .andExpect(jsonPath("$.createdDate", is(job1.getCreatedDate().toString())));
        verify(jobService, times(1)).saveJob(job1);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void getAllJobs() throws Exception {
        List<Job> jobs = Arrays.asList(job1, job2);
        when(jobService.getAllJobs()).thenReturn(jobs);
        mockMvc.perform(get("/jobs"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].jobName", is(job1.getJobName())))
                .andExpect(jsonPath("$[1].jobName", is(job2.getJobName())));
        verify(jobService, times(1)).getAllJobs();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void getJobById_jobExists() throws Exception {
        when(jobService.findById(job1.getId())).thenReturn(Optional.of(job1));
        mockMvc.perform(get("/jobs/{id}", job1.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.jobName", is(job1.getJobName())));
        verify(jobService, times(1)).findById(job1.getId());
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void getJobById_jobDoesNotExist() throws Exception {
        when(jobService.findById(job1.getId())).thenReturn(null);
        mockMvc.perform(get("/jobs/{id}", job1.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));
        verify(jobService, times(1)).findById(job1.getId());
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void deleteJobById_jobExists() throws Exception {
        doNothing().when(jobService).deleteJob(job1.getId());
        mockMvc.perform(delete("/jobs/{id}", job1.getId()))
                .andExpect(status().is2xxSuccessful());
        verify(jobService, times(1)).deleteJob(job1.getId());
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void deleteJobById_jobDoesNotExist() throws Exception {
        doThrow(new JobNotFoundException("Job not found")).when(jobService).deleteJob(job1.getId());
        mockMvc.perform(delete("/jobs/{id}", job1.getId()))
                .andExpect(status().isNotFound());
        verify(jobService, times(1)).deleteJob(job1.getId());
        verifyNoMoreInteractions(jobService);
    }

}
