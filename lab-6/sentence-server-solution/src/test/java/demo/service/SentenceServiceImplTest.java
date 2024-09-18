package demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import demo.dao.AdjectiveClient;
import demo.dao.ArticleClient;
import demo.dao.NounClient;
import demo.dao.SubjectClient;
import demo.dao.VerbClient;
import demo.domain.Word;

import static org.assertj.core.api.Assertions.assertThat;

public class SentenceServiceImplTest {

	//	Class under test:
	SentenceServiceImpl service;

	@BeforeEach
	public void setup() {

		service = new SentenceServiceImpl();

		//	Establish Mock Dependencies:
		SubjectClient subject = Mockito.mock(SubjectClient.class);
		VerbClient verb = Mockito.mock(VerbClient.class);
		ArticleClient article = Mockito.mock(ArticleClient.class);
		AdjectiveClient adjective = Mockito.mock(AdjectiveClient.class);
		NounClient noun = Mockito.mock(NounClient.class);

		service.setSubjectService(subject);
		service.setVerbService(verb);
		service.setArticleService(article);
		service.setAdjectiveService(adjective);
		service.setNounService(noun);

		//	Describe Mock Behaviors:
		Mockito.when(subject.getWord()).thenReturn(new Word("I"));
		Mockito.when(verb.getWord())	.thenReturn(new Word("have"));
		Mockito.when(article.getWord())	.thenReturn(new Word("a"));
		Mockito.when(adjective.getWord()).thenReturn(new Word("good"));
		Mockito.when(noun.getWord())	.thenReturn(new Word("feeling"));
	}

	@Test
	public void test() {
		//	We should get the sentence built in the correct order:
		assertThat(service.buildSentence()).isEqualTo("I have a good feeling.");
	}

}
