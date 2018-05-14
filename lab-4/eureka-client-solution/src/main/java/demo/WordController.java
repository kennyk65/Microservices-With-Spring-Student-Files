package demo;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordController {

    private final Random random = new Random();
	
	@Value("${words}") String[] words;
	
	@GetMapping("/")
	public String getWord() {
		return words[random.nextInt(words.length)];
	}

}
