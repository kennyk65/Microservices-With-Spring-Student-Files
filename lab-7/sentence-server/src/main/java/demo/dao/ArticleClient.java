package demo.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import demo.domain.Word;

@FeignClient("ARTICLE")
public interface ArticleClient {

	@GetMapping("/")
	Word getWord();

}
