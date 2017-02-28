package cap.ddg.hackapi.event;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This repository provides CRUD operations for {@link Event}
 * objects.
 *
 */
interface EventRepository extends Repository<Event, String> {

    /**
     * Deletes a event entry from the database.
     * @param deleted   The deleted event entry.
     */
    void delete(Event deleted);

    /**
     * Finds all event entries from the database.
     * @return  The information of all event entries that are found from the database.
     */
    List<Event> findAll();

    /**
     * Finds the information of a single event entry.
     * @param id    The id of the requested event entry.
     * @return      The information of the found event entry. If no event entry
     *              is found, this method returns an empty {@link Optional} object.
     */
    Optional<Event> findOne(String id);

    /**
     * Saves a new event entry to the database.
     * @param saved The information of the saved event entry.
     * @return      The information of the saved event entry.
     */
    Event save(Event saved);
}
