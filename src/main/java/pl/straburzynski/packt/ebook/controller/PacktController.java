package pl.straburzynski.packt.ebook.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.service.EbookService;
import pl.straburzynski.packt.ebook.service.SlackService;

import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/packt")
public class PacktController {

    private final EbookService ebookService;
    private final SlackService slackService;

    @Autowired
    public PacktController(EbookService ebookService, SlackService slackService) {
        this.ebookService = ebookService;
        this.slackService = slackService;
    }

    @GetMapping("today-ebook")
    @ApiOperation("Get today free ebook data")
    public ResponseEntity<?> getTodayFreeEbookData() {
        Ebook ebook = ebookService.getTodayFreeEbookDataFromPackt();
        return new ResponseEntity<>(ebook, HttpStatus.OK);
    }

    @PostMapping("send-to-slack")
    @ApiOperation("Send ebook message to slack with default configuration")
    public ResponseEntity<?> send() throws URISyntaxException {
        slackService.sendMessageToSlack();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("check-credentials")
    @ApiOperation("Check if login/password are correct")
    public ResponseEntity<?> checkCredentials() {
        return new ResponseEntity<>(ebookService.checkLogin(), HttpStatus.OK);
    }

    @PostMapping("claim-ebook")
    @ApiOperation("Claim free ebook from Packt")
    public ResponseEntity<?> claimEbook() {
        ebookService.claimFreeEbookFromPackt();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
