package cap.ddg.hackapi.event;


import java.util.List;

/**
 * This interface declares the methods that provides CRUD operations for
 * {@link Event} objects.
 *
 */
interface EventService {

    EventDTO create(EventDTO event);

    EventDTO delete(String id);

    List<EventDTO> findAll();

    EventDTO findById(String id);

    List<EventCount> countByTeam(String teamName);


}
