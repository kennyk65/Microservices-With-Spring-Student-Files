package demo.dao;

import org.springframework.stereotype.Component;

@Component("subjectService")
public class SubjectDaoImpl extends WordDaoImpl {

	@Override
	public String getPartOfSpeech() {
		return SUBJECT;
	}

	
}
