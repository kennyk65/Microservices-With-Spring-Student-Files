package demo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This class is used to simulate a delay in the application.  It is
 * only used when the "delay" profile is active.
 */

@Component
@Profile("delay")
public class DelayProducer {

    // write a method that produces a delay between 0 and 2 seconds.:
    public void delay() {
        try {
            Thread.sleep((long) (Math.random() * 2000) );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
