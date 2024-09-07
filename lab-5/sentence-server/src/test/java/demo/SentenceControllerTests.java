package demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;

public class SentenceControllerTests {

    @Mock
    private DiscoveryClient discoveryClient;

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
        // Mock the URI for the services
        URI subjectUri = URI.create("http://subject-service");
        URI verbUri = URI.create("http://verb-service");
        URI articleUri = URI.create("http://article-service");
        URI adjectiveUri = URI.create("http://adjective-service");
        URI nounUri = URI.create("http://noun-service");

        // Mock the ServiceInstance for each service
        ServiceInstance subjectInstance = mock(ServiceInstance.class);
        ServiceInstance verbInstance = mock(ServiceInstance.class);
        ServiceInstance articleInstance = mock(ServiceInstance.class);
        ServiceInstance adjectiveInstance = mock(ServiceInstance.class);
        ServiceInstance nounInstance = mock(ServiceInstance.class);

        // Configure mock behavior for DiscoveryClient and RestTemplate
        when(discoveryClient.getInstances("SUBJECT")).thenReturn(Collections.singletonList(subjectInstance));
        when(discoveryClient.getInstances("VERB")).thenReturn(Collections.singletonList(verbInstance));
        when(discoveryClient.getInstances("ARTICLE")).thenReturn(Collections.singletonList(articleInstance));
        when(discoveryClient.getInstances("ADJECTIVE")).thenReturn(Collections.singletonList(adjectiveInstance));
        when(discoveryClient.getInstances("NOUN")).thenReturn(Collections.singletonList(nounInstance));

        when(subjectInstance.getUri()).thenReturn(subjectUri);
        when(verbInstance.getUri()).thenReturn(verbUri);
        when(articleInstance.getUri()).thenReturn(articleUri);
        when(adjectiveInstance.getUri()).thenReturn(adjectiveUri);
        when(nounInstance.getUri()).thenReturn(nounUri);

        when(restTemplate.getForObject(subjectUri, String.class)).thenReturn("I");
        when(restTemplate.getForObject(verbUri, String.class)).thenReturn("see");
        when(restTemplate.getForObject(articleUri, String.class)).thenReturn("an");
        when(restTemplate.getForObject(adjectiveUri, String.class)).thenReturn("amazing");
        when(restTemplate.getForObject(nounUri, String.class)).thenReturn("future");

        // Exercise the method under test
        String sentence = sentenceController.buildSentence();

        // Verify the result
        assertEquals("I see an amazing future.", sentence);
    }
}
