package pl.straburzynski.packt.ebook.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.net.URISyntaxException;

@Slf4j
@Service
public class SendEbookJob implements Job {

    @Autowired
    private SlackService slackService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap map = context.getMergedJobDataMap();
        try {
            doTask(map);
        } catch (URISyntaxException e) {
            log.debug("Error parsing URL from task job id " + map.getString("jobId"));
        }
    }

    private void doTask(JobDataMap map) throws URISyntaxException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        Long jobId = Long.parseLong(map.getString("jobId"));
        log.info("Parsing JobDataMap, task: " + jobId);
        slackService.sendMessageToSlack(jobId);
    }

}
