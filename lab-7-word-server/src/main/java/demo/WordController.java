package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Controller
public class WordController {

	@Value("${words}") String words;
//    String words = "icicle,refrigerator,blizzard,snowball";
	
	@RequestMapping("/")
	@HystrixCommand(fallbackMethod="another")
	public @ResponseBody Word getWord() {
		String[] wordArray = words.split(",");
		int i = (int)Math.round(Math.random() * (wordArray.length - 1));
		return new Word(wordArray[i]);
	}
	
	public Word another() {
		return getWord();
	}
}
