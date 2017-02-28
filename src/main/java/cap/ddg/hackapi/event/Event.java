package cap.ddg.hackapi.event;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@Document(collection = "event")
final class Event {

    @Id
    private String id;

    private String team;

    private String endpoint;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Date dateTime;

    public Event() {}

    private Event(Builder builder) {
        this.team = builder.team;
        this.endpoint = builder.endpoint;
//        long timeNow = System.currentTimeMillis();
//        String pattern = "MM/dd/yyyy HH:mm:ss";
//        SimpleDateFormat df = new SimpleDateFormat(pattern);
        this.dateTime = new Date();//df.format( new Date(timeNow) );

    }

    static Builder getBuilder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getTeam() {
        return team;
    }

    public String getEndpoint() { return endpoint; }

    public Date getDateTime() { return dateTime; }

    public void update(String team, String endpoint, Date dateTime) {
        //checkTitleAndDescription(title, description);

        this.team = team;
        this.dateTime = dateTime;
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return String.format(
                "Event[id=%s, team=%s, endpoint=%s, dateTime=%s]",
                this.id,
                this.team,
                this.endpoint,
                this.dateTime
        );
    }

    /**
     * We don't have to use the builder pattern here because the constructed class has only two String fields.
     * However, I use the builder pattern in this example because it makes the code a bit easier to read.
     */
    static class Builder {

        private String team;
        private String endpoint;
        private Date dateTime;

        private Builder() {}

        Builder team(String team) {
            this.team = team;
            return this;
        }

        Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        Builder dateTime(Date dateTime) {
            this.dateTime = dateTime;
            return this;
        }


        Event build() {
            Event build = new Event(this);
            return build;
        }
    }


}
