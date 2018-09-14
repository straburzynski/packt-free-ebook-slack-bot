package pl.straburzynski.packt.ebook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.straburzynski.packt.ebook.config.ApplicationConfig;
import pl.straburzynski.packt.ebook.exception.JobNotFoundException;
import pl.straburzynski.packt.ebook.exception.SendingMessageToSlackException;
import pl.straburzynski.packt.ebook.model.*;
import pl.straburzynski.packt.ebook.model.slack.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlackServiceImpl implements SlackService {

    private final String BOOK_ICON = ":book:";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JobService jobService;
    private final ApplicationConfig applicationConfig;
    private final EbookService ebookService;

    @Autowired
    public SlackServiceImpl(EbookService ebookService, ApplicationConfig applicationConfig, JobService jobService) {
        this.ebookService = ebookService;
        this.applicationConfig = applicationConfig;
        this.jobService = jobService;
    }

    private Message prepareSlackMessage(Ebook ebook) {
        return Message.builder()
                .username(applicationConfig.getBotName())
                .icon_emoji(BOOK_ICON)
                .attachments(prepareAttachments(ebook))
                .build();
    }

    private Message prepareSlackMessage(Ebook ebook, Job job) {
        return Message.builder()
                .username(job.getBotName() != null ? job.getBotName() : applicationConfig.getBotName())
                .icon_emoji(BOOK_ICON)
                .attachments(prepareAttachments(ebook))
                .build();
    }

    private List<Attachment> prepareAttachments(Ebook ebook) {
        List<Attachment> attachments = new ArrayList<>();
        attachments.add(Attachment.builder()
                .title(ebook.getTitle())
                .title_link(ebook.getBookUrl())
                .color(String.valueOf(Color.BLUE))
                .text(ebook.getDescription())
                .build());
        attachments.add(Attachment.builder()
                .title(ebook.getTitle())
                .image_url(prepareUrl(ebook.getImageUrl()))
                .thumb_url(prepareUrl(ebook.getImageUrl()))
                .color(String.valueOf(Color.ORANGE))
                .actions(prepareActions(ebook.getBookUrl()))
                .build());
        return attachments;
    }

    private List<Action> prepareActions(String ebookUrl) {
        List<Action> actions = new ArrayList<>();
        actions.add(Action.builder()
                .type(String.valueOf(Type.BUTTON))
                .text("I want this book")
                .url(applicationConfig.getPacktFreeEbookUrl())
                .style(String.valueOf(Style.PRIMARY))
                .build());
        actions.add(Action.builder()
                .type(String.valueOf(Type.BUTTON))
                .text("Show me more details")
                .url(prepareUrl(ebookUrl))
                .style(String.valueOf(Style.DEFAULT))
                .build());
        return actions;
    }
    
    private String prepareUrl(String url) {
        return url.replace(" ", "%20");
    }

    private void doRestTemplateExchange(Ebook ebook, Message message, RestTemplate restTemplate, URI uri) {
        RequestEntity<?> request = new RequestEntity<>(message, HttpMethod.POST, uri);
        ResponseEntity<?> response = restTemplate.exchange(request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Message sent successfully. " + ebook.toString().replace("\n", " "));
        } else {
            String error = "Error sending message to slack:"
                    + response.getStatusCode() + " " + response.getBody() + "Ebook: " + ebook.toString();
            log.error(error);
            throw new SendingMessageToSlackException(error);
        }
    }

    @Override
    @Scheduled(cron = "${app.scheduler-time}")
    public void sendMessageToSlack() throws URISyntaxException {
        Ebook ebook = ebookService.getTodayFreeEbookDataFromPackt();
        Message message = prepareSlackMessage(ebook);
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(applicationConfig.getSlackWebhookUrl());
        doRestTemplateExchange(ebook, message, restTemplate, uri);
    }


    @Override
    public void sendMessageToSlack(Long jobId) throws URISyntaxException {
        log.info("Sending ebook to slack, job no: " + jobId);
        Job job = jobService.findById(jobId).orElseThrow(
                () -> new JobNotFoundException("Job " + jobId + "not found!")
        );
        if (job.isActive()) {
            log.info("Task with job " + jobId + " is ready to execute");
            Ebook ebook = ebookService.getTodayFreeEbookDataFromPackt();
            Message message = prepareSlackMessage(ebook, job);
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(job.getWebhook());
            doRestTemplateExchange(ebook, message, restTemplate, uri);
        } else {
            log.info("Task with job " + jobId + " is out of date range or disabled");
        }
    }

}
