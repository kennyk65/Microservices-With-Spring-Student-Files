package demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import demo.domain.Player;

@RestResource(rel="player", path="players")
public interface PlayerDao extends CrudRepository<Player, Long> {

}
