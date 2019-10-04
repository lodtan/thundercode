package Model;

import java.util.Date;

public abstract class Post {
    protected int id;
    protected Date creationDate;
    protected int score;
    protected String body;
    protected Date lastActivityDate;

    public Post(int id, Date creationDate, int score, String body, Date lastActivityDate) {
        this.id = id;
        this.creationDate = creationDate;
        this.score = score;
        this.body = body;
        this.lastActivityDate = lastActivityDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }


}