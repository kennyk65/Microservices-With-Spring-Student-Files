package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuckyWordController {

	@Autowired CustomMetrics metrics;
	
	@Value("${lucky-word}") String luckyWord;
	  
	@GetMapping("/lucky-word")
	public String showLuckyWord() {

		// TODO-09: Before returning the lucky word, add code to
		//	call the metrics.incrementLuckyWordMetric():

		
		return "The lucky word is: " + luckyWord;
	}
}
