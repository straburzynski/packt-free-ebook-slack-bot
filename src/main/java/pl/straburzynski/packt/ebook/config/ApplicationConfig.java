package pl.straburzynski.packt.ebook.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Getter
public class ApplicationConfig {

    @Value("${packt.free-ebook-url}")
    private String packtFreeEbookUrl;

    @Value("${packt.url}")
    private String packtBaseUrl;

    @Value("${app.bot-name}")
    private String botName;

    @Value("${app.slack-webhook-url}")
    private String slackWebhookUrl;

    @Value("${packt.user.email}")
    private String userEmail;

    @Value("${packt.user.password}")
    private String userPassword;

}
