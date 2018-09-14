package pl.straburzynski.packt.ebook.controller;

import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.straburzynski.packt.ebook.model.Job;
import pl.straburzynski.packt.ebook.service.JobService;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @ApiOperation("Create new job")
    public ResponseEntity<?> createJob(@RequestBody Job job) throws ParseException, SchedulerException {
        return new ResponseEntity<>(jobService.saveJob(job), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Edit job")
    public ResponseEntity<?> editJob(@RequestBody Job job, @PathVariable("id") Long id) throws ParseException, SchedulerException {
        job.setId(id);
        return new ResponseEntity<>(jobService.editJob(job), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("Get all jobs")
    public ResponseEntity<?> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @ApiOperation("Get job by id")
    public ResponseEntity<?> getJobById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete job by id")
    public ResponseEntity<?> deleteJobById(@PathVariable("id") Long id) throws ParseException, SchedulerException {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
