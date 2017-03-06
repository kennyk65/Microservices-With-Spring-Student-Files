package demo.service;


import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.dao.AdjectiveClient;
import demo.dao.ArticleClient;
import demo.dao.NounClient;
import demo.dao.SubjectClient;
import demo.dao.VerbClient;
import demo.domain.Word;
import demo.domain.Word.Role;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author Ken Krueger, Jorge Centeno Fernandez 
 */
@Service
public class WordServiceImpl implements WordService {

	@Autowired VerbClient verbClient;
	@Autowired SubjectClient subjectClient;
	@Autowired ArticleClient articleClient;
	@Autowired AdjectiveClient adjectiveClient;
	@Autowired NounClient nounClient;
	@Autowired Executor executor;	//	Source of threads
	
	
	@Override
	@HystrixCommand(fallbackMethod="getFallbackSubject")
	public Observable<Word> getSubject() {
		//	This 'reactive' observable is backed by a regular Java Callable, which can run in a different thread:
		return Observable.fromCallable(
			() ->  new Word (subjectClient.getWord().getWord(), Role.subject)
		).subscribeOn(Schedulers.from(executor));
	}
	
	@Override
	@HystrixCommand(fallbackMethod="getFallbackVerb")
	public Observable<Word> getVerb() {
		return Observable.fromCallable(
			() ->  new Word (verbClient.getWord().getWord(), Role.verb)
		).subscribeOn(Schedulers.from(executor));
	}
	
	@Override
	@HystrixCommand(fallbackMethod="getFallbackArticle")
	public Observable<Word> getArticle() {
		return Observable.fromCallable(
			() ->  new Word (articleClient.getWord().getWord(), Role.article)
		).subscribeOn(Schedulers.from(executor));
	}
	
	@Override
	@HystrixCommand(fallbackMethod="getFallbackAdjective")
	public Observable<Word> getAdjective() {
		return Observable.fromCallable(
			() ->  new Word (adjectiveClient.getWord().getWord(), Role.adjective)
		).subscribeOn(Schedulers.from(executor));
	}
	
	@Override
	@HystrixCommand(fallbackMethod="getFallbackNoun")
	public Observable<Word> getNoun() {
		return Observable.fromCallable(
			() ->  new Word (nounClient.getWord().getWord(), Role.noun)
		).subscribeOn(Schedulers.from(executor));
	}	
	
	
	
	public Word getFallbackSubject() {
		return new Word("Someone", Role.subject);
	}
	
	public Word getFallbackVerb() {
		return new Word("does", Role.verb);
	}
	
	public Word getFallbackArticle() {
		return new Word("", Role.article);
	}
	
	public Word getFallbackAdjective() {
		return new Word("", Role.adjective);
	}
	
	public Word getFallbackNoun() {
		return new Word("something", Role.noun);
	}

}
