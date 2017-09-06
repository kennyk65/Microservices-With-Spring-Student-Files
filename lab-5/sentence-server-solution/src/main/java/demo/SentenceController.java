package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;



@Controller
public class SentenceController {

	//	This is referencing the RestTemplate we defined earlier:
	@Autowired  RestTemplate template;
	
	/**
	 * Display a small list of Sentences to the caller:
	 */
	@GetMapping("/sentence")
	public @ResponseBody String getSentence() {
	  return 
		"<h3>Some Sentences</h3><br/>" +	  
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>" +
		buildSentence() + "<br/><br/>"
		;
	}

	/**
	 * Assemble a sentence by gathering random words of each part of speech:
	 */
	public String buildSentence() {
		String sentence = "There was a problem assembling the sentence!";
		try{
			sentence =  
				String.format("%s %s %s %s %s.",
					getWord("SUBJECT"),
					getWord("VERB"),
					getWord("ARTICLE"),
					getWord("ADJECTIVE"),
					getWord("NOUN") );			
		} catch ( Exception e ) {
			System.out.println(e);
		}
		return sentence;
	}
	
	/**
	 * Obtain a random word for a given part of speech, where the part 
	 * of speech is indicated by the given service / client ID:
	 */
	public String getWord(String service) {
		return template.getForObject("http://" + service, String.class);
	
	}

}
