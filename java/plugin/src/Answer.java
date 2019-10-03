import java.util.Date;

public class Answer extends Post {
    protected int parentId;

    public Answer(int id, Date creationDate, int score, String body, Date lastActivityDate, int parentId) {
        super(id, creationDate, score, body, lastActivityDate);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
