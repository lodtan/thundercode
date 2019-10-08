package Model;

import java.util.ArrayList;
import java.util.Date;

public class Question extends Post {
    protected String title;
    protected String tags;
    protected int viewCount;
    protected int acceptedAnswerId;


    public Question(int id, Date creationDate, int score, String body, Date lastActivityDate, String title, String tags, int viewCount, int acceptedAnswerId) {
        super(id, creationDate, score, body, lastActivityDate);
        this.title = title;
        this.tags = tags;
        this.viewCount = viewCount;
        this.acceptedAnswerId = acceptedAnswerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

}
