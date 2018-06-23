package pl.straburzynski.packt.ebook.controller;

import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.straburzynski.packt.ebook.model.scheduler.JobDescriptor;
import pl.straburzynski.packt.ebook.service.SchedulerJobService;

import java.text.ParseException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/scheduled-jobs")
@RequiredArgsConstructor
public class SchedulerJobController {

    private final SchedulerJobService schedulerJobService;

    @PostMapping()
    public ResponseEntity<JobDescriptor> createJob(@RequestBody JobDescriptor descriptor) {
        return new ResponseEntity<>(schedulerJobService.createJob(descriptor), CREATED);
    }

    @PostMapping(path = "/stopAll")
    public ResponseEntity<?> stopAllJobs() {
        schedulerJobService.stopAllJobs();
        return new ResponseEntity<>(OK);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<JobDescriptor> findJob(@PathVariable String name) {
        return schedulerJobService.findJob(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<?> findAllJobs() throws SchedulerException, ParseException {
        return schedulerJobService.findAllJobs()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity<Void> deleteJob(@PathVariable String name) {
        schedulerJobService.deleteJob(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/start")
    public ResponseEntity<?> startAllPushNotifications() throws SchedulerException, ParseException {
        return schedulerJobService.startAllActivePacktJobs()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/restart")
    public ResponseEntity<?> restartAllPushNotifications() throws SchedulerException, ParseException {
        return schedulerJobService.restartAllActivePacktJobs()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
