package demo.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import demo.dao.AdjectiveDaoImpl;
import demo.dao.ArticleDaoImpl;
import demo.dao.NounDaoImpl;
import demo.dao.SubjectDaoImpl;
import demo.dao.VerbDaoImpl;
import demo.dao.WordDao;
import demo.domain.Word;

public class SentenceServiceImplTest {

	//	Class under test:
	SentenceServiceImpl service;
	
	@BeforeEach
	public void setup() {

		service = new SentenceServiceImpl();
		
		//	Establish Mock Dependencies:
		WordDao subject = Mockito.mock(SubjectDaoImpl.class);
		WordDao verb = Mockito.mock(VerbDaoImpl.class);
		WordDao article = Mockito.mock(ArticleDaoImpl.class);
		WordDao adjective = Mockito.mock(AdjectiveDaoImpl.class);
		WordDao noun = Mockito.mock(NounDaoImpl.class);

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
