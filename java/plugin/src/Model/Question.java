package Model;

import java.util.ArrayList;
import java.util.Date;

public class Question extends Post {
    protected String title;
    protected ArrayList<String> tags;


    public Question(int id, Date creationDate, int score, String body, Date lastActivityDate, String title, ArrayList<String> tags) {
        super(id, creationDate, score, body, lastActivityDate);
        this.title = title;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

}
