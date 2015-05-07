package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
//@ConfigurationProperties(prefix="wordConfig")
public class LuckyWordController {
	 
	@Value("${LuckyWord}") String luckyWord;
//	String luckyWord;
	  
	  @RequestMapping("/lucky-word")
	  public String showLuckyWord() {
	    return "The lucky word is: " + luckyWord;
	  }

//	public String getLuckyWord() {
//		return luckyWord;
//	}
//
//	public void setLuckyWord(String luckyWord) {
//		this.luckyWord = luckyWord;
//	}
}
