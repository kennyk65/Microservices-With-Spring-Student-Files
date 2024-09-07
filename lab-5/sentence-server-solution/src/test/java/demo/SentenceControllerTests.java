package demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

public class SentenceControllerTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SentenceController sentenceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSentence() {
        when(restTemplate.getForObject("http://SUBJECT", String.class)).thenReturn("I");
        when(restTemplate.getForObject("http://VERB", String.class)).thenReturn("see");
        when(restTemplate.getForObject("http://ARTICLE", String.class)).thenReturn("an");
        when(restTemplate.getForObject("http://ADJECTIVE", String.class)).thenReturn("amazing");
        when(restTemplate.getForObject("http://NOUN", String.class)).thenReturn("future");

        // Exercise the method under test
        String sentence = sentenceController.buildSentence();

        // Verify the result
        assertEquals("I see an amazing future.", sentence);
    }
}
