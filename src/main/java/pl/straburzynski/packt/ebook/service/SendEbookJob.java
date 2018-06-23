package pl.straburzynski.packt.ebook.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Slf4j
public class SendEbookJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap map = context.getMergedJobDataMap();
        doTask(map);
    }

    private void doTask(JobDataMap map) {
        Long jobId = Long.parseLong(map.getString("jobId"));
        String webhook = map.getString("webhook");
        log.info("--- execute job: " + jobId + " --- webhook: " + webhook);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("send ebook to slack, job no: " + jobId);
    }
}
