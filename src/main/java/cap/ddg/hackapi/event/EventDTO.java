package cap.ddg.hackapi.event;


import java.util.Date;

public final class EventDTO {

    private String id;

    private String team;

    private String endpoint;

    private Date dateTime;

    public EventDTO() {

    }

    public String getId() {
        return id;
    }

    public String getTeam() {
        return team;
    }

    public String getEndpoint() { return endpoint; }

    public Date getDateTime() {
        return dateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return String.format(
                "EventDTO[id=%s, team=%s, endpoint=%s, dateTime=%s]",
                this.id,
                this.team,
                this.endpoint,
                this.dateTime
        );
    }
}
