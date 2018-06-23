package pl.straburzynski.packt.ebook.service;

import org.quartz.CronExpression;
import org.springframework.stereotype.Component;
import pl.straburzynski.packt.ebook.exception.JobValidationException;
import pl.straburzynski.packt.ebook.model.Job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidationService {

    private JobService jobService;

    public ValidationService(JobService jobService) {
        this.jobService = jobService;
    }

    void validate(Job job) {
        validateJobName(job.getJobName());
        validateWebHook(job.getWebhook());
        validateScheduler(job.getScheduler());
    }

    private void validateJobName(String jobName) {
        if (jobService.findByJobName(jobName).isPresent()) {
            throw new JobValidationException("Job name already exists");
        }
    }

    private void validateWebHook(String webhook) {
        Pattern p = Pattern.compile("^http(s)?://hooks.slack.com/services/.{9}/.{9}/.{24}");
        Matcher m = p.matcher(webhook);
        if (!m.matches()) {
            throw new JobValidationException("Webhook url is not valid");
        }
    }

    private void validateScheduler(String cronExpression) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new JobValidationException("Cron expression is not valid");
        }
    }

}
