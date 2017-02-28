package cap.ddg.hackapi.vote;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 2/22/17.
 */
@Document(collection = "team")
public class Team {

    @Id
    private String team;

    private Map<String, Vote> votes = new HashMap<String, Vote>(5);

    private int total;

    private List<Message> messageList = new ArrayList<>(5);



    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Message> getMessages() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public void addMessage(Message msg){
        messageList.add(msg);
    }

    public void addVote(Vote vote){
        if(votes.containsKey(vote.getFromTeam()))
        {
            total = total - (votes.get(vote.getFromTeam())).getValue();
            votes.replace(vote.getFromTeam(), vote);
        }
        else{
            votes.putIfAbsent(vote.getFromTeam(), vote);
        }

        total = total + vote.getValue();

    }

    public Vote getVote(String teamName){
        if(votes.containsKey(teamName))
        {
            return votes.get(teamName);
        }
        else
        {
            return new Vote();
        }
    }

    public Map<String, Vote> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return String.format(
                "Team[team=%s, total=%s]",
                this.team,
                this.total
        );
    }
}
