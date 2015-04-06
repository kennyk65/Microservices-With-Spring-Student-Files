package demo.domain;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Team {

	String name;
	String mascotte;
	String location;
	Set<Player> players;
	
	
	public Team() {
		super();
	}
	
	public Team(String location, String name, Set<Player> players) {
		this();
		this.location = location;
		this.name = name;
		this.players = players;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMascotte() {
		return mascotte;
	}
	public void setMascotte(String mascotte) {
		this.mascotte = mascotte;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
	
	
}
