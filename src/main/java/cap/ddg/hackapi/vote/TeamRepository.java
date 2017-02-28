package cap.ddg.hackapi.vote;


import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This repository provides CRUD operations for {@link Team}
 * objects.
 *
 */
interface TeamRepository extends Repository<Team, String> {

    /**
     * Deletes a team entry from the database.
     * @param deleted   The deleted team entry.
     */
    void delete(Team deleted);


    /**
     * Finds all teams entries from the database.
     * @return  The information of all teams entries that are found from the database.
     */
    List<Team> findAll();

    /**
     * Finds the information of a single team entry.
     * @param id    The id of the requested event entry.
     * @return      The information of the found team entry. If no team entry
     *              is found, this method returns an empty {@link Optional} object.
     */
    Optional<Team> findOne(String id);

    /**
     * Saves a new team entry to the database.
     * @param saved The information of the saved team entry.
     * @return      The information of the saved team entry.
     */
    Team save(Team saved);
}
