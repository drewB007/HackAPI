package cap.ddg.hackapi.event;

/**
 * This exception is thrown when the requested event entry is not found.
 *
 */
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String id) {
        super(String.format("No event entry found with id: <%s>", id));
    }
}
