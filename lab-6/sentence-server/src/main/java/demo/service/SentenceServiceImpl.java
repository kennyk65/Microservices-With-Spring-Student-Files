package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.dao.AdjectiveDaoImpl;
import demo.dao.ArticleDaoImpl;
import demo.dao.NounDaoImpl;
import demo.dao.SubjectDaoImpl;
import demo.dao.VerbDaoImpl;

/**
 * Build a sentence by assembling randomly generated subjects, verbs, 
 * articles, adjectives, and nouns.  The individual parts of speech will 
 * be obtained by calling the various DAOs.
 */
@Service
public class SentenceServiceImpl implements SentenceService {

	@Autowired VerbDaoImpl verbService;
	@Autowired SubjectDaoImpl subjectService;
	@Autowired ArticleDaoImpl articleService;
	@Autowired AdjectiveDaoImpl adjectiveService;
	@Autowired NounDaoImpl nounService;
	

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
