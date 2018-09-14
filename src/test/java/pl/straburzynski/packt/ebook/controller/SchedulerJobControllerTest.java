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
import pl.straburzynski.packt.ebook.model.scheduler.JobDescriptor;
import pl.straburzynski.packt.ebook.service.SchedulerJobService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.straburzynski.packt.ebook.utils.ObjectConverter.asJsonString;

public class SchedulerJobControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SchedulerJobService schedulerJobService;

    @InjectMocks
    private SchedulerJobController schedulerJobController;

    private JobDescriptor jobDescriptor1 = JobDescriptor.builder()
            .name("Name 1")
            .group("Group 1")
            .jobId("1")
            .chrono("* 30 13 ? * * *")
            .webhook("http://test.com/x/y/z")
            .build();

    private JobDescriptor jobDescriptor2 = JobDescriptor.builder()
            .name("Name 2")
            .group("Group 2")
            .jobId("2")
            .chrono("* 30 13 ? * * *")
            .webhook("http://test.com/x/y/z")
            .build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(schedulerJobController).build();
    }

    @Test
    public void createJob_fromJobDescriptor() throws Exception {
        String jobDescriptor1Json = asJsonString(jobDescriptor1);
        when(schedulerJobService.createJob(jobDescriptor1)).thenReturn(jobDescriptor1);
        mockMvc.perform(
                post("/scheduled-jobs")
                        .characterEncoding(CharEncoding.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(jobDescriptor1Json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(jobDescriptor1.getName())));
        verify(schedulerJobService, times(1)).createJob(jobDescriptor1);
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void stopAllJobs() throws Exception {
        doNothing().when(schedulerJobService).stopAllJobs();
        mockMvc.perform(post("/scheduled-jobs/stopAll"))
                .andExpect(status().is2xxSuccessful());
        verify(schedulerJobService, times(1)).stopAllJobs();
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void findJob_jobExists() throws Exception {
        String name = "Name 1";
        when(schedulerJobService.findJob(name)).thenReturn(Optional.of(jobDescriptor1));
        mockMvc.perform(
                get("/scheduled-jobs/{name}", name)
                        .characterEncoding(CharEncoding.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(jobDescriptor1.getName())));
        verify(schedulerJobService, times(1)).findJob(name);
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void findJob_jobDoesNotExist() throws Exception {
        String name = "Test";
        when(schedulerJobService.findJob(name)).thenReturn(Optional.empty());
        mockMvc.perform(
                get("/scheduled-jobs/{name}", name)
                        .characterEncoding(CharEncoding.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
        verify(schedulerJobService, times(1)).findJob(name);
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void findAllJobs_jobsExists() throws Exception {
        List<String> jobDescriptorList = Arrays.asList(jobDescriptor1.getName(), jobDescriptor2.getName());
        when(schedulerJobService.findAllJobs()).thenReturn(Optional.of(jobDescriptorList));
        mockMvc.perform(get("/scheduled-jobs"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(jobDescriptor1.getName())))
                .andExpect(jsonPath("$[1]", is(jobDescriptor2.getName())));
        verify(schedulerJobService, times(1)).findAllJobs();
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void deleteJob_jobExists() throws Exception {
        doNothing().when(schedulerJobService).deleteJob(jobDescriptor1.getName());
        mockMvc.perform(delete("/scheduled-jobs/{name}", jobDescriptor1.getName()))
                .andExpect(status().is2xxSuccessful());
        verify(schedulerJobService, times(1)).deleteJob(jobDescriptor1.getName());
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void deleteJob_jobDoesNotExist() throws Exception {
        String name = "Test";
        doThrow(new JobNotFoundException("Job not found")).when(schedulerJobService).deleteJob(name);
        mockMvc.perform(delete("/scheduled-jobs/{name}", name))
                .andExpect(status().isNotFound());
        verify(schedulerJobService, times(1)).deleteJob(name);
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void startAllPushNotifications_jobsExists() throws Exception {
        List<String> jobDescriptorList = Arrays.asList(jobDescriptor1.getName(), jobDescriptor2.getName());
        when(schedulerJobService.startAllActivePacktJobs()).thenReturn(Optional.of(jobDescriptorList));
        mockMvc.perform(post("/scheduled-jobs/start"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*", hasSize(2)));
        verify(schedulerJobService, times(1)).startAllActivePacktJobs();
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void startAllPushNotifications_jobsDoesNotExist() throws Exception {
        when(schedulerJobService.startAllActivePacktJobs()).thenReturn(Optional.empty());
        mockMvc.perform(post("/scheduled-jobs/start"))
                .andExpect(status().isNotFound());
        verify(schedulerJobService, times(1)).startAllActivePacktJobs();
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void restartAllPushNotifications_jobsExists() throws Exception {
        List<String> jobDescriptorList = Arrays.asList(jobDescriptor1.getName(), jobDescriptor2.getName());
        when(schedulerJobService.restartAllActivePacktJobs()).thenReturn(Optional.of(jobDescriptorList));
        mockMvc.perform(post("/scheduled-jobs/restart"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*", hasSize(2)));
        verify(schedulerJobService, times(1)).restartAllActivePacktJobs();
        verifyNoMoreInteractions(schedulerJobService);
    }

    @Test
    public void restartAllPushNotifications_jobsDoesNotExist() throws Exception {
        when(schedulerJobService.restartAllActivePacktJobs()).thenReturn(Optional.empty());
        mockMvc.perform(post("/scheduled-jobs/restart"))
                .andExpect(status().isNotFound());
        verify(schedulerJobService, times(1)).restartAllActivePacktJobs();
        verifyNoMoreInteractions(schedulerJobService);
    }

}