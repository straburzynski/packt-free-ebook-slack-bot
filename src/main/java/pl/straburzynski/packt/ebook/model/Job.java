package pl.straburzynski.packt.ebook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job")
public class Job {

    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @GeneratedValue
    private long id;

    @Column(name = "job_name", unique = true, updatable = true, nullable = false)
    private String jobName;

    @Column(name = "bot_name", unique = false, updatable = true, nullable = true)
    private String botName;

    @Column(name = "channel", unique = false, updatable = true, nullable = true)
    private String channel;

    @Column(name = "webhook", unique = false, updatable = true, nullable = false)
    private String webhook;

    @Column(name = "scheduler", unique = false, updatable = true, nullable = false)
    private String scheduler;

    @Column(name = "active", unique = false, updatable = true, nullable = false)
    private boolean active;

    @Column(name = "created_date", unique = false, updatable = true, nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "start_date", unique = false, updatable = true, nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", unique = false, updatable = true, nullable = true)
    private LocalDateTime endDate;



}
