package cap.ddg.hackapi.vote;

import cap.ddg.hackapi.event.EventDTO;

import java.util.List;

/**
 * Created by andrew on 2/22/17.
 */
interface TeamService {


    Team create(Team team);

    Team update(Team team);

    Team delete(String id);

    List<Team> findAll();

    Team findById(String id);

}