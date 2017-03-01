package cap.ddg.hackapi.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.List;
import java.util.Optional;
import com.revinate.guava.util.concurrent.RateLimiter;


import static java.util.stream.Collectors.toList;

/**
 * This service class saves {@link Event} objects
 * to MongoDB database.
 *
 */
@Service
final class MongoDBEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBEventService.class);

    private final EventRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

    private RateLimiter ratelimiter = null;

    @Value("${txn.limit}")
    private Double rateLimit;

    @Autowired
    MongoDBEventService(EventRepository repository) {
        this.repository = repository;
    }

    private void lazyLoadLimiter(){

        try{
            if(ratelimiter == null) {
                this.ratelimiter = RateLimiter.create(rateLimit);
                LOGGER.info("Created new rate limiter with rate: {}", ratelimiter.getRate());
            }
            else{
                LOGGER.info("Using existing rate limiter with rate: {}", ratelimiter.getRate());
            }
        }
        catch(Exception ex){
            this.ratelimiter = RateLimiter.create(2);
        }

    }

    public List<EventCount> countByTeam(String teamName){

        Aggregation agg = newAggregation(match(Criteria.where("team").is(teamName)),group("endpoint").count().as("count"),project("count").and("endpoint").previousOperation(),sort(Sort.Direction.DESC, "count") );

        AggregationResults<EventCount> groupResults = mongoTemplate.aggregate(agg,"event",EventCount.class);
        List<EventCount> result = groupResults.getMappedResults();

        return result;
    }

//    Aggregation agg = newAggregation(
//            project()
//                    .andExpression("year(timeCreated)").as("year")
//                    .andExpression("month(timeCreated)").as("month")
//                    .andExpression("dayOfMonth(timeCreated)").as("day"),
//            group(fields().and("year").and("month").and("day"))
//                    .sum("blabla").as("blabla")
//    );
//
//    AggregationResults<BlaBlaModel> result =
//            mongoTemplate.aggregate(agg, collectionName, BlaBlaModel.class);
//    List<BlaBlaModel> resultList = result.getMappedResults();

    @Override
    public EventDTO create(EventDTO event) {
        LOGGER.info("Creating a new event entry with information: {}", event);
        lazyLoadLimiter();
        ratelimiter.acquire();

        Event persisted = Event.getBuilder()
                .team(event.getTeam())
                .dateTime(event.getDateTime())
                .endpoint(event.getEndpoint())
                .build();

        persisted = repository.save(persisted);
        LOGGER.info("Created a new event entry with information: {}", persisted);

        return convertToDTO(persisted);
    }

    @Override
    public EventDTO delete(String id) {
        LOGGER.info("Deleting a event entry with id: {}", id);

        Event deleted = findEventById(id);
        repository.delete(deleted);

        LOGGER.info("Deleted event entry with informtation: {}", deleted);

        return convertToDTO(deleted);
    }

    @Override
    public List<EventDTO> findAll() {
        LOGGER.info("Finding all event entries.");

        List<Event> eventEntries = repository.findAll();

        LOGGER.info("Found {} event entries", eventEntries.size());

        return convertToDTOs(eventEntries);
    }

    private List<EventDTO> convertToDTOs(List<Event> models) {
        return models.stream()
                .map(this::convertToDTO)
                .collect(toList());
    }

    @Override
    public EventDTO findById(String id) {
        LOGGER.info("Finding event entry with id: {}", id);

        Event found = findEventById(id);

        LOGGER.info("Found event entry: {}", found);

        return convertToDTO(found);
    }


    private Event findEventById(String id) {
        Optional<Event> result = repository.findOne(id);
        return result.orElseThrow(() -> new EventNotFoundException(id));

    }

    private EventDTO convertToDTO(Event model) {
        EventDTO dto = new EventDTO();

        dto.setId(model.getId());
        dto.setTeam(model.getTeam());
        dto.setEndpoint(model.getEndpoint());
        dto.setDateTime(model.getDateTime());

        return dto;
    }
}
