package demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import demo.dao.AdjectiveClient;
import demo.dao.ArticleClient;
import demo.dao.NounClient;
import demo.dao.SubjectClient;
import demo.dao.VerbClient;
import demo.domain.Word;

@Service
public class WordServiceImpl implements WordService {

	@Autowired VerbClient verbClient;
	@Autowired SubjectClient subjectClient;
	@Autowired ArticleClient articleClient;
	@Autowired AdjectiveClient adjectiveClient;
	@Autowired NounClient nounClient;
	@Autowired CircuitBreakerFactory circuitBreakers;
	
	@Override
	public Word getSubject() {
		return
			circuitBreakers
				.create("subject")
				.run(
					() -> subjectClient.getWord(),
					(throwable) -> getFallbackSubject()
				);
	}
	
	@Override
	public Word getVerb() {
		return verbClient.getWord();
	}
	
	@Override
	public Word getArticle() {
		return articleClient.getWord();
	}
	
	@Override
	public Word getAdjective() {
		return
			circuitBreakers
				.create("adjective")
				.run(
					() -> adjectiveClient.getWord(),
					(throwable) -> getFallbackAdjective()
				);
	}
	
	@Override
	public Word getNoun() {
		return
			circuitBreakers
				.create("noun")
				.run(
					() -> nounClient.getWord(),
					(throwable) -> getFallbackNoun()
				);
	}

	public Word getFallbackSubject() {
		return new Word("Someone");
	}
	
	public Word getFallbackAdjective() {
		return new Word("");
	}
	
	public Word getFallbackNoun() {
		return new Word("something");
	}

}
