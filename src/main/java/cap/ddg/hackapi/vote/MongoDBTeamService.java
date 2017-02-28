package cap.ddg.hackapi.vote;

import cap.ddg.hackapi.event.*;
import com.revinate.guava.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * This service class saves {@link Team} objects
 * to MongoDB database.
 *
 */
@Service
final class MongoDBTeamService implements TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBTeamService.class);

    private final TeamRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;


    @Autowired
    MongoDBTeamService(TeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Team update(Team team){
        //repository.update()
        Team updated = repository.save(team);

        return updated;
    }

    @Override
    public Team create(Team team) {
        LOGGER.info("Creating a new team entry with information: {}", team);

        Team persisted = repository.save(team);
        LOGGER.info("Created a new team entry with information: {}", persisted);

        return persisted;
    }

    @Override
    public Team delete(String id) {
        LOGGER.info("Deleting a team entry with id: {}", id);

        Team deleted = findTeamById(id);
        repository.delete(deleted);

        LOGGER.info("Deleted team entry with information: {}", deleted);

        return deleted;
    }

    @Override
    public List<Team> findAll() {
        LOGGER.info("Finding all team entries.");

        List<Team> teams = repository.findAll();

        LOGGER.info("Found {} team entries", teams.size());

        return teams;
    }

    @Override
    public Team findById(String id) {
        LOGGER.info("Finding team entry with id: {}", id);

        Team found;
        try {
            found = findTeamById(id);
            LOGGER.info("Found team entry: {}", found);
        }
        catch(TeamNotFoundException tnfex){
            Team newTeam = new Team();
            newTeam.setTeam(id);
            found = create(newTeam);
            LOGGER.info("Created team entry: {}", found);

        }

        return found;
    }


    private Team findTeamById(String id) {
        Optional<Team> result = repository.findOne(id);
        return result.orElseThrow(() -> new TeamNotFoundException(id));

    }


}
