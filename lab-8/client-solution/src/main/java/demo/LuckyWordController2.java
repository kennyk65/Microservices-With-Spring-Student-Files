package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller demonstrates @RefreshScope alternative to @ConfigurationProperties
 */
@RestController
@RefreshScope
public class LuckyWordController2 {
	 
	//  Notice how the prefix of "wordConfig" 
	//  must be repeated without @ConfigurationProperties:	
	@Value("${wordConfig.luckyWord}") String luckyWord;
	@Value("${wordConfig.preamble}") String preamble;
	
	@GetMapping("/lucky-word2")
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
