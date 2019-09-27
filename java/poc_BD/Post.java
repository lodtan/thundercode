import java.sql.Timestamp;

public abstract class Post {
    protected int id;
    protected Timestamp creationDate;
    protected int score;
    protected String body;
    protected int ownerUserId;
    protected String lastEditorDisplayName;
    protected Timestamp lastEditDate;
    protected Timestamp lastActivityDate;
    protected int commentCount;

    public Post(int id, Timestamp creationDate, int score, String body, int ownerUserId, String lastEditorDisplayName, Timestamp lastEditDate, Timestamp lastActivityDate, int commentCount) {
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
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

    public Timestamp getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Timestamp lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Timestamp getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Timestamp lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}