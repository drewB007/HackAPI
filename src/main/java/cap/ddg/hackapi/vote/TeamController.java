package cap.ddg.hackapi.vote;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller provides the public API that is used to manage the information
 * of team entries.
 *
 */
@RestController
@RequestMapping("/api/team")
final class TeamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);

    private final TeamService service;

    @Autowired
    TeamController(TeamService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    Team create(@RequestBody @Valid Team team) {

        LOGGER.info("Creating a new team entry with information: {}", team);
        Team created = service.create(team);
        LOGGER.info("Created a new team entry with information: {}", created);

        return created;

    }


    @RequestMapping(value = "/message/{forTeamName}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    Message addTeamMessage(@RequestBody @Valid Message msg, @PathVariable("forTeamName") String forTeamName) {

        LOGGER.info("Finding team entry with teamName: {}", forTeamName);
        Team found = service.findById(forTeamName);
        LOGGER.info("Found team entry with information: {}", found);

        found.addMessage(msg);
        update(found,forTeamName);
        LOGGER.info("Added a message for: {}", found);

        return msg;

    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    Message broadcastMessage(@RequestBody @Valid Message msg) {

        LOGGER.info("Finding teams");
        List<Team> teams = service.findAll();
        LOGGER.info("Found {} teams", teams.size());

        for (Team team: teams){
            addTeamMessage(msg,team.getTeam());
        }

        return msg;

    }


    @RequestMapping(value = "{forTeamName}/vote/{fromTeamName}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    Vote createVote(@RequestBody @Valid Vote vote, @PathVariable("forTeamName") String forTeamName, @PathVariable("fromTeamName") String fromTeamName) {

        LOGGER.info("Finding team entry with teamName: {}", forTeamName);
        Team found = service.findById(forTeamName);
        LOGGER.info("Found team entry with information: {}", found);

        found.addVote(vote);
        update(found,forTeamName);
        LOGGER.info("Added a new vote for: {}", found);

        Message msg = new Message();
        msg.setSender(fromTeamName);
        msg.setType("INFO");
        msg.setText(fromTeamName + " just cast a vote for you!");

        addTeamMessage(msg, forTeamName);

        return vote;

    }

    @RequestMapping(value = "{forTeamName}/vote/{fromTeamName}", method = RequestMethod.GET)
    Vote getVote(@PathVariable("forTeamName") String forTeamName, @PathVariable("fromTeamName") String fromTeamName) {
        LOGGER.info("Finding team entry with teamName: {}", forTeamName);

        Team found = service.findById(forTeamName);
        LOGGER.info("Found team entry with information: {}", found);

        Vote vote = found.getVote(fromTeamName);

        return vote;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    Team update(@RequestBody @Valid Team team, @PathVariable("id") String id) {
        LOGGER.info("Updating team entry with id: {}", id);

        Team updated = service.update(team);
        LOGGER.info("Updated team entry with information: {}", updated);

        return updated;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    Team delete(@PathVariable("id") String id) {
        LOGGER.info("Deleting team entry with id: {}", id);

        Team deleted = service.delete(id);
        LOGGER.info("Deleted team entry with information: {}", deleted);

        return deleted;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Team> findAll() {
        LOGGER.info("Finding all team entries");

        List<Team> teams = service.findAll();
        LOGGER.info("Found {} team entries", teams.size());

        return teams;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    Team fmindById(@PathVariable("id") String id) {
        LOGGER.info("Finding team entry with id: {}", id);

        Team team = service.findById(id);
        LOGGER.info("Found team entry with information: {}", team);

        return team;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleTeamNotFound(TeamNotFoundException ex) {
        LOGGER.error("Handling error with message: {}", ex.getMessage());
    }

}
