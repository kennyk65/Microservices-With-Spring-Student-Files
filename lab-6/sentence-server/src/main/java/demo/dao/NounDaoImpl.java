package demo.dao;

import org.springframework.stereotype.Component;

@Component
public class NounDaoImpl extends WordDaoImpl {

	@Override
	public String getPartOfSpeech() {
		return NOUN;
	}

	
}
