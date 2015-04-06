package demo.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import demo.domain.Team;

public interface TeamDao extends CrudRepository<Team, Long> {

	List<Team> findAll();
	
	Team findById(Long id); 
	
	Team findByName(String name);
}
