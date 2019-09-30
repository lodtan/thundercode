package Model;

import java.util.Date;

public class Answer extends Post{
    protected int parentId;

    public Answer(int id, Date creationDate, int score, String body, int ownerUserId, String lastEditorDisplayName, Date lastEditDate, Date lastActivityDate, int commentCount, int parentId) {
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
