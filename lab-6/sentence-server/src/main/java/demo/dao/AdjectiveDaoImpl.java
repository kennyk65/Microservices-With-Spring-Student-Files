package demo.dao;

import org.springframework.stereotype.Component;

@Component
public class AdjectiveDaoImpl extends WordDaoImpl {

	@Override
	public String getPartOfSpeech() {
		return ADJECTIVE;
	}

	
}
