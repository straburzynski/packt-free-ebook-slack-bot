package pl.straburzynski.packt.ebook.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.service.EbookService;
import pl.straburzynski.packt.ebook.service.SlackService;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/free-ebook")
public class EbookController {

    private final EbookService ebookService;
    private final SlackService slackService;

    @Autowired
    public EbookController(EbookService ebookService, SlackService slackService) {
        this.ebookService = ebookService;
        this.slackService = slackService;
    }

    @GetMapping
    @ApiOperation("Get today free ebook data")
    public ResponseEntity<?> getTodayFreeEbookData() {
        Ebook ebook = ebookService.getTodayFreeEbookDataFromPackt();
        return new ResponseEntity<>(ebook, HttpStatus.OK);
    }

    @PostMapping("/send")
    @ApiOperation("Send ebook message to slack with default configuration")
    public ResponseEntity<?> send() throws URISyntaxException {
        slackService.sendMessageToSlack();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
