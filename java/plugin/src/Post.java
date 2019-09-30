import java.util.Date;

public abstract class Post {
    protected int id;
    protected Date creationDate;
    protected int score;
    protected String body;
    protected int ownerUserId;
    protected String lastEditorDisplayName;
    protected Date lastEditDate;
    protected Date lastActivityDate;
    protected int commentCount;

    public Post(int id, Date creationDate, int score, String body, int ownerUserId, String lastEditorDisplayName, Date lastEditDate, Date lastActivityDate, int commentCount) {
        this.id = id;
        this.creationDate = creationDate;
        this.score = score;
        this.body = body;
        this.ownerUserId = ownerUserId;
        this.lastEditorDisplayName = lastEditorDisplayName;
        this.lastEditDate = lastEditDate;
        this.lastActivityDate = lastActivityDate;
        this.commentCount = commentCount;
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

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getLastEditorDisplayName() {
        return lastEditorDisplayName;
    }

    public void setLastEditorDisplayName(String lastEditorDisplayName) {
        this.lastEditorDisplayName = lastEditorDisplayName;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}