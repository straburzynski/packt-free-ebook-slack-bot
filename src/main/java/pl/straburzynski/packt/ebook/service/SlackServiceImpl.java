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
import pl.straburzynski.packt.ebook.exception.SendingMessageToSlackException;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.model.SlackAction;
import pl.straburzynski.packt.ebook.model.SlackAttachment;
import pl.straburzynski.packt.ebook.model.SlackMessage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlackServiceImpl implements SlackService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ApplicationConfig applicationConfig;
    private final EbookService ebookService;

    @Autowired
    public SlackServiceImpl(EbookService ebookService, ApplicationConfig applicationConfig) {
        this.ebookService = ebookService;
        this.applicationConfig = applicationConfig;
    }

    @Override
    @Scheduled(cron = "0 0 13 ? * *")
    public void sendMessageToSlack() throws URISyntaxException {
        Ebook ebook = ebookService.getTodayFreeEbookDataFromPackt();
        SlackMessage slackMessage = prepareSlackMessage(ebook);

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(applicationConfig.getSlackWebhookUrl());
        RequestEntity<?> request = new RequestEntity<>(slackMessage, HttpMethod.POST, uri);
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

    private SlackMessage prepareSlackMessage(Ebook ebook) {
        return SlackMessage.builder()
                .username(applicationConfig.getBotName())
                .icon_emoji(":book:")
                .attachments(prepareAttachments(ebook))
                .build();
    }

    private List<SlackAttachment> prepareAttachments(Ebook ebook) {
        List<SlackAttachment> attachments = new ArrayList<>();
        attachments.add(SlackAttachment.builder()
                .title(ebook.getTitle())
                .title_link(ebook.getBookUrl())
                .color("#007bff")
                .text(ebook.getDescription())
                .build());
        attachments.add(SlackAttachment.builder()
                .text(ebook.getTitle())
                .image_url(ebook.getImageUrl())
                .thumb_url(ebook.getImageUrl())
                .color("#f36e28")
                .actions(prepareActions(ebook.getBookUrl()))
                .build());
        return attachments;
    }

    private List<SlackAction> prepareActions(String ebookUrl) {
        List<SlackAction> actions = new ArrayList<>();
        actions.add(SlackAction.builder()
                .type("button")
                .text("I want this book")
                .url(applicationConfig.getPacktFreeEbookUrl())
                .style("primary")
                .build());
        actions.add(SlackAction.builder()
                .type("button")
                .text("Show me more details")
                .url(ebookUrl)
                .style("default")
                .build());
        return actions;
    }

}
