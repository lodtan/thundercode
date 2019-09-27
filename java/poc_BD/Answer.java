import java.sql.Timestamp;

public class Answer extends Post{
    protected int parentId;

    public Answer(int id, Timestamp creationDate, int score, String body, int ownerUserId, String lastEditorDisplayName, Timestamp lastEditDate, Timestamp lastActivityDate, int commentCount, int parentId) {
        super(id, creationDate, score, body, ownerUserId, lastEditorDisplayName, lastEditDate, lastActivityDate, commentCount);
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
