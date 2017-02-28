package cap.ddg.hackapi.vote;

/**
 * Created by andrew on 2/22/17.
 */
public class Vote {

    private String fromTeam;

    private int value;

    public String getFromTeam() {
        return fromTeam;
    }

    public void setFromTeam(String fromTeam) {
        this.fromTeam = fromTeam;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(
                "Vote[fromTeam=%s, value=%s]",
                this.fromTeam,
                this.value
        );
    }
}
