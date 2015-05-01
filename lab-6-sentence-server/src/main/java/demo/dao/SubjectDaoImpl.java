package demo.dao;

import org.springframework.stereotype.Component;

@Component
public class SubjectDaoImpl extends WordDaoImpl {

	@Override
	public String getPartOfSpeech() {
		return SUBJECT;
	}

	
}
