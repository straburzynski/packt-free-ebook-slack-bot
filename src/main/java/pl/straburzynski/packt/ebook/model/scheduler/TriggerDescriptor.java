package pl.straburzynski.packt.ebook.model.scheduler;

import lombok.Data;
import org.quartz.JobDataMap;
import org.quartz.Trigger;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static java.time.ZoneId.systemDefault;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.StringUtils.isEmpty;

@Data
public class TriggerDescriptor {

    @NotNull
    private String name;
    private String group;
    private LocalDateTime fireTime;
    private String cron;

    public Trigger buildTrigger(String name, String chrono) {
        if (!isEmpty(chrono)) {
            if (!isValidExpression(chrono))
                throw new IllegalArgumentException("Provided expression " + chrono + " is not a valid cron expression");
            return newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(cronSchedule(chrono)
                            .withMisfireHandlingInstructionFireAndProceed()
                            .inTimeZone(TimeZone.getTimeZone(systemDefault())))
                    .usingJobData("cron", chrono)
                    .build();
        } else if (!isEmpty(fireTime)) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("fireTime", fireTime);
            return newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(simpleSchedule()
                            .withMisfireHandlingInstructionNextWithExistingCount())
                    .startAt(Date.from(fireTime.atZone(systemDefault()).toInstant()))
                    .usingJobData(jobDataMap)
                    .build();
        }
        throw new IllegalStateException("Unsupported trigger descriptor " + this);
    }

}
