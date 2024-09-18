package demo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("exception || exceptions")
public class ExceptionProducer {

    // Randomly throw an exception 50% of the time.
    public void produceException() {

        // Flip a coin, 0 or 1;
        int coinFlip = (int) Math.round(Math.random());
        if (coinFlip == 1) {
            throw new RuntimeException("This is a simulated exception");
        }
    }
}
