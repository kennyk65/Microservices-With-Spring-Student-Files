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

	@Autowired VerbClient verbService;
	@Autowired SubjectClient subjectService;
	@Autowired ArticleClient articleService;
	@Autowired AdjectiveClient adjectiveService;
	@Autowired NounClient nounService;
	

	/**
	 * Assemble a sentence by gathering random words of each part of speech:
	 */
	public String buildSentence() {
		String sentence = "There was a problem assembling the sentence!";
			sentence = 
				subjectService.getWord().getString() + " "
					+ verbService.getWord().getString() + " "
					+ articleService.getWord().getString() + " "
					+ adjectiveService.getWord().getString() + " "
					+ nounService.getWord().getString() + " "
			;
		return sentence;
	}	
}
