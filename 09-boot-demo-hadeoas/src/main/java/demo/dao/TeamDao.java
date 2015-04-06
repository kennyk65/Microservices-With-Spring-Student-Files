package demo.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import demo.domain.Team;

@RestResource(rel="team", path="teams")
public interface TeamDao extends CrudRepository<Team, Long> {

	List<Team> findAll();
	
	Team findById(Long id); 
	
	Team findByName(String name);
}
