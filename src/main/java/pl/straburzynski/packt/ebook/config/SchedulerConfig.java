package pl.straburzynski.packt.ebook.config;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import pl.straburzynski.packt.ebook.service.SchedulerJobService;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log
@Configuration
public class SchedulerConfig {

    private final SchedulerJobService schedulerJobService;

    @Autowired
    public SchedulerConfig(@Lazy SchedulerJobService schedulerJobService) {
        this.schedulerJobService = schedulerJobService;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factoryBean.setJobFactory(jobFactory);
        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return factoryBean;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executeAfterStartup() throws ParseException, SchedulerException {
        log.info("Application started: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        schedulerJobService.startAllActivePacktJobs();
    }

}
