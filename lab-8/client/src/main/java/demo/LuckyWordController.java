package demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TODO: Add @ConfigurationProperties here.
public class LuckyWordController {
	 
	String luckyWord;
	String preamble;
	
	@GetMapping("/lucky-word")
	public String showLuckyWord() {
		return preamble + ": " + luckyWord;
	}

	public String getLuckyWord() {
		return luckyWord;
	}

	public void setLuckyWord(String luckyWord) {
		this.luckyWord = luckyWord;
	}

	public String getPreamble() {
		return preamble;
	}

	public void setPreamble(String preamble) {
		this.preamble = preamble;
	}
}
