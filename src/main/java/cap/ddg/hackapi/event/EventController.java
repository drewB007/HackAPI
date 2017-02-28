package cap.ddg.hackapi.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller provides the public API that is used to manage the information
 * of event entries.
 *
 */
@RestController
@RequestMapping("/api/event")
final class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService service;

    @Value("${team.name}")
    private String thisTeam;

    @Autowired
    EventController(EventService service) {
        this.service = service;
    }

    @RequestMapping(value="/count/{team}", method = RequestMethod.GET)
    List<EventCount> count(@PathVariable("team") String team) {
        LOGGER.info("Counting events for team: {}", team);


        return service.countByTeam(team);
    }


    @RequestMapping(value="/generate", method = RequestMethod.GET)
    EventDTO create() {
        String endpoint = "GET: /api/event/generate";
        LOGGER.info("Creating a new event entry for endpoint: {}", endpoint);


        return createEvent(endpoint);

    }

    private EventDTO createEvent(String endpoint){
        String teamName = thisTeam;
        EventDTO event = new EventDTO();
        event.setTeam(teamName);
        event.setEndpoint(endpoint);

        EventDTO created = service.create(event);
        LOGGER.info("Created a new event entry with information: {}", created);
        return created;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    EventDTO delete(@PathVariable("id") String id) {
        LOGGER.info("Deleting event entry with id: {}", id);

        EventDTO deleted = service.delete(id);
        LOGGER.info("Deleted event entry with information: {}", deleted);

        return deleted;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<EventDTO> findAll() {
        LOGGER.info("Finding all event entries");

        List<EventDTO> eventEntries = service.findAll();
        LOGGER.info("Found {} event entries", eventEntries.size());

        return eventEntries;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    EventDTO findById(@PathVariable("id") String id) {
        LOGGER.info("Finding event entry with id: {}", id);

        EventDTO eventEntry = service.findById(id);
        LOGGER.info("Found event entry with information: {}", eventEntry);

        return eventEntry;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEventNotFound(EventNotFoundException ex) {
        LOGGER.error("Handling error with message: {}", ex.getMessage());
    }
}
