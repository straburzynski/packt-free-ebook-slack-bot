package pl.straburzynski.packt.ebook.service;

public interface SlackService {

    void sendMessageToSlack() throws Exception;

    void sendMessageToSlack(Long jobId) throws Exception;

}
