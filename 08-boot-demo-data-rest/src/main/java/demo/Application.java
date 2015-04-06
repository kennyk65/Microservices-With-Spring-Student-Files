package demo;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import demo.dao.TeamDao;
import demo.domain.Player;
import demo.domain.Team;

@SpringBootApplication
@Import(RepositoryRestMvcConfiguration.class)
public class Application extends SpringBootServletInitializer {

    /**
     * Required for JAR deployment
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Required for WAR deployment
     */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
    
	@Autowired TeamDao teamDao;
	
	@PostConstruct
	public void init() {
		
		Set<Player> s = new HashSet<>();
		s.add(new Player("Charlie Brown",  "pitcher"));
		s.add(new Player("Snoopy", "shortstop"));
		
		Team team = new Team("California", "Peanuts", s);
		
		teamDao.save(team);
	}
	
}
