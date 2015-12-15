package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.dao.AdjectiveClient;
import demo.dao.ArticleClient;
import demo.dao.NounClient;
import demo.dao.SubjectClient;
import demo.dao.VerbClient;

/**
 * Build a sentence by assembling randomly generated subjects, verbs, 
 * articles, adjectives, and nouns.  The individual parts of speech will 
 * be obtained by calling the various DAOs.
 */
@Service
public class SentenceServiceImpl implements SentenceService {

	VerbClient verbService;
	SubjectClient subjectService;
	ArticleClient articleService;
	AdjectiveClient adjectiveService;
	NounClient nounService;
	

	/**
	 * Assemble a sentence by gathering random words of each part of speech:
	 */
	public String buildSentence() {
		String sentence = "There was a problem assembling the sentence!";
		sentence =  
			String.format("%s %s %s %s %s.",
				subjectService.getWord().getString(),
				verbService.getWord().getString(),
				articleService.getWord().getString(),
				adjectiveService.getWord().getString(),
				nounService.getWord().getString() );
		return sentence;
	}

	@Autowired
	public void setVerbService(VerbClient verbService) {
		this.verbService = verbService;
	}

	@Autowired
	public void setSubjectService(SubjectClient subjectService) {
		this.subjectService = subjectService;
	}

	@Autowired
	public void setArticleService(ArticleClient articleService) {
		this.articleService = articleService;
	}

	@Autowired
	public void setAdjectiveService(AdjectiveClient adjectiveService) {
		this.adjectiveService = adjectiveService;
	}

	@Autowired
	public void setNounService(NounClient nounService) {
		this.nounService = nounService;
	}	
	
}
