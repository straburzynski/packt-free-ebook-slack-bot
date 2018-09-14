package pl.straburzynski.packt.ebook.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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

    @Column(name = "job_name", unique = true, nullable = false)
    private String jobName;

    @Column(name = "bot_name")
    private String botName;

    @Column(name = "channel")
    private String channel;

    @Column(name = "webhook", nullable = false)
    private String webhook;

    @Column(name = "scheduler", nullable = false)
    private String scheduler;

    @Column(name = "active", nullable = false)
    private boolean active;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

}
