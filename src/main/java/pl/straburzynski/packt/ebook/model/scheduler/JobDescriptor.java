package pl.straburzynski.packt.ebook.model.scheduler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import pl.straburzynski.packt.ebook.service.SendEbookJob;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JobDescriptor {

    private String name;
    private String group;
    private String jobId;
    private String chrono;
    private String webhook;
    private String startDate;
    private String endDate;
    private Map<String, Object> data = new LinkedHashMap<>();

    public static JobDescriptor buildDescriptor(JobDetail jobDetail) {
        return JobDescriptor.builder()
                .name(jobDetail.getKey().getName())
                .group(jobDetail.getKey().getGroup())
                .jobId(jobDetail.getJobDataMap().getString("jobId"))
                .chrono(jobDetail.getJobDataMap().getString("chrono"))
                .webhook(jobDetail.getJobDataMap().getString("webhook"))
                .startDate(jobDetail.getJobDataMap().getString("startDate"))
                .endDate(jobDetail.getJobDataMap().getString("endDate"))
                .build();
    }

    public Set<Trigger> buildTriggers(String triggerName, String triggerChrono) {
        Set<Trigger> triggers = new LinkedHashSet<>();
        TriggerDescriptor triggerDescriptor = new TriggerDescriptor();
        triggers.add(triggerDescriptor.buildTrigger(triggerName, triggerChrono));
        return triggers;
    }

    public JobDetail buildJobDetail(String selectedGroup) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobId", jobId);
        jobDataMap.put("webhook", webhook);
        jobDataMap.put("chrono", chrono);
        jobDataMap.put("startDate", startDate);
        jobDataMap.put("endDate", endDate);
        return newJob(SendEbookJob.class)
                .withIdentity(name, selectedGroup)
                .usingJobData(jobDataMap)
                .build();
    }
}
