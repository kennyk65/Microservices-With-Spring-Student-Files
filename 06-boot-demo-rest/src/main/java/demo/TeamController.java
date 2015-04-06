package demo;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.domain.Player;
import demo.domain.Team;

@RestController
public class TeamController {

	Team team;
	
	@PostConstruct
	public void init() {
		
		Player p1 = new Player("Charlie Brown",  "pitcher");
		Player p2 = new Player("Snoopy", "shortstop");
		
		Set<Player> s = new HashSet<>();
		s.add(new Player("Charlie Brown",  "pitcher"));
		s.add(new Player("Snoopy", "shortstop"));
		
		team = new Team("California", "Peanuts", s);
		
	}

	@RequestMapping("/teams/1")
	public Team getTeam() {
		return team;
	}
	
}
