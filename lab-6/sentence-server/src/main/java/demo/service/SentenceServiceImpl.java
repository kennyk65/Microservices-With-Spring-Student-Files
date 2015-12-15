package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.dao.WordDao;

/**
 * Build a sentence by assembling randomly generated subjects, verbs, 
 * articles, adjectives, and nouns.  The individual parts of speech will 
 * be obtained by calling the various DAOs.
 */
@Service
public class SentenceServiceImpl implements SentenceService {

	private WordDao verbService;
	private WordDao subjectService;
	private WordDao articleService;
	private WordDao adjectiveService;
	private WordDao nounService;
	

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
	public void setVerbService(WordDao verbService) {
		this.verbService = verbService;
	}

	@Autowired
	public void setSubjectService(WordDao subjectService) {
		this.subjectService = subjectService;
	}

	@Autowired
	public void setArticleService(WordDao articleService) {
		this.articleService = articleService;
	}

	@Autowired
	public void setAdjectiveService(WordDao adjectiveService) {
		this.adjectiveService = adjectiveService;
	}

	@Autowired
	public void setNounService(WordDao nounService) {
		this.nounService = nounService;
	}	
	
	
}
