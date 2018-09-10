package pl.straburzynski.packt.ebook.service;

import java.net.URISyntaxException;

public interface SlackService {

    void sendMessageToSlack() throws URISyntaxException;

    void sendMessageToSlack(Long jobId) throws URISyntaxException;

}
