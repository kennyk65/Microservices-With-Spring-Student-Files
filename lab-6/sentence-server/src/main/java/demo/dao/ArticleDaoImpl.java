package demo.dao;

import org.springframework.stereotype.Component;

@Component
public class ArticleDaoImpl extends WordDaoImpl {

	@Override
	public String getPartOfSpeech() {
		return ARTICLE;
	}

	
}
