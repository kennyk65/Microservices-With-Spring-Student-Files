package demo;


import jakarta.annotation.PostConstruct;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;


@Component
public class CustomMetrics {

    private Counter LuckyWordRequests;

    //  TODO-06: Add a private final member variable of type MeterRegistry.
    //  Add a constructor to inject this value:


    // TODO-07: Add a @PostConstruct method to instantiate the existing
    // LuckyWordRequests metric counter.  Call the meterRegistry's counter()
    // method to create the new counter. Provide it with the name "custom.lucky-word-requests.counter".


    //  TODO-08: Create a public void "incrementLuckyWordMetric()" method.
    //  Within, have it call the increment() method on the LuckyWordRequests counter.
    
}
