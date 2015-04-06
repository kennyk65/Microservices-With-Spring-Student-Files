package demo;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.dao.TeamDao;
import demo.domain.Player;
import demo.domain.Team;

@RestController
public class TeamController {

	@Autowired TeamDao teamDao;
	
	@PostConstruct
	public void init() {
		
		Set<Player> s = new HashSet<>();
		s.add(new Player("Charlie Brown",  "pitcher"));
		s.add(new Player("Snoopy", "shortstop"));
		
		Team team = new Team("California", "Peanuts", s);
		
		teamDao.save(team);
	}

	@RequestMapping("/teams/{teamname}")
	public Team getTeam(@PathVariable String teamname) {
		return teamDao.findByName(teamname);
	}
	
}
