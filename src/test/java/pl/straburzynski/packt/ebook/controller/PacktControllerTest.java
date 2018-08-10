package pl.straburzynski.packt.ebook.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.straburzynski.packt.ebook.exception.InvalidEbookException;
import pl.straburzynski.packt.ebook.exception.LoginFailedException;
import pl.straburzynski.packt.ebook.model.Ebook;
import pl.straburzynski.packt.ebook.service.EbookService;
import pl.straburzynski.packt.ebook.service.SlackService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PacktControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EbookService ebookService;

    @Mock
    private SlackService slackService;

    @InjectMocks
    private PacktController packtController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(packtController).build();
    }

    @Test
    public void getTodayFreeEbookData_successfully() throws Exception {
        Ebook ebook = Ebook.builder()
                .title("Test title")
                .bookUrl("http://www.packtpub.com/application-development/test-ebook")
                .description("Test description")
                .imageUrl("https://test.com/123.png").build();
        when(ebookService.getTodayFreeEbookDataFromPackt()).thenReturn(ebook);
        mockMvc.perform(get("/packt/today-ebook"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title", is(ebook.getTitle())))
                .andExpect(jsonPath("$.bookUrl", is(ebook.getBookUrl())))
                .andExpect(jsonPath("$.description", is(ebook.getDescription())))
                .andExpect(jsonPath("$.imageUrl", is(ebook.getImageUrl())));
        verify(ebookService, times(1)).getTodayFreeEbookDataFromPackt();
        verifyNoMoreInteractions(ebookService);
    }

    @Test
    public void getTodayFreeEbookData_emptyEbookData() throws Exception {
        doThrow(new InvalidEbookException("Description is empty")).when(ebookService).getTodayFreeEbookDataFromPackt();
        mockMvc.perform(get("/packt/today-ebook"))
                .andExpect(status().isInternalServerError());
        verify(ebookService, times(1)).getTodayFreeEbookDataFromPackt();
        verifyNoMoreInteractions(ebookService);
    }

    @Test
    public void sendMessageToSlack_successfully() throws Exception {
        doNothing().when(slackService).sendMessageToSlack();
        mockMvc.perform(
                post("/packt/send-to-slack")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is2xxSuccessful());
        verify(slackService, times(1)).sendMessageToSlack();
        verifyNoMoreInteractions(slackService);
    }

    @Test
    public void checkCredentials_userExists() throws Exception {
        String user = "Joe Doe";
        when(ebookService.checkLogin()).thenReturn(user);
        mockMvc.perform(
                post("/packt/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(user));
        verify(ebookService, times(1)).checkLogin();
        verifyNoMoreInteractions(ebookService);
    }

    @Test
    public void checkCredentials_userDoesNotExist() throws Exception {
        when(ebookService.checkLogin()).thenThrow(new LoginFailedException("Login failed"));
        mockMvc.perform(
                post("/packt/check-credentials")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
        verify(ebookService, times(1)).checkLogin();
        verifyNoMoreInteractions(ebookService);
    }

    @Test
    public void claimEbook_successfully() throws Exception {
        doNothing().when(ebookService).claimFreeEbookFromPackt();
        mockMvc.perform(
                post("/packt/claim-ebook")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is2xxSuccessful());
        verify(ebookService, times(1)).claimFreeEbookFromPackt();
        verifyNoMoreInteractions(ebookService);
    }

}
