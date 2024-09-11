package demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class SentenceController {

	@GetMapping("/")
	public String sentence() {
		return "sentence";
	}

}
