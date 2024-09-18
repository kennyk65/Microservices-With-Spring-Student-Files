package demo;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordController {

    private final Random random = new Random();

	@Autowired	Optional<DelayProducer> delayProducer;
	@Autowired  Optional<ExceptionProducer> exceptionProducer;
	@Value("${words}") String[] words;
	
	@GetMapping("/")
	public Word getWord() {

		// If the exceptionProducer is present, call it to produce exceptions 50% of the time.
		exceptionProducer.ifPresent(ExceptionProducer::produceException);

		// If the delayProducer is present, a 0 to 2 second delay will be experienced.
		delayProducer.ifPresent(DelayProducer::delay);

		return
			new Word(
				words[random.nextInt(words.length)]
			);
	}
}
