package Model;

import java.util.ArrayList;
import java.util.Date;

public class Question extends Post {
    protected String title;
    protected ArrayList<String> tags;
    protected int answerCount;
    protected int favoriteCount;

    public Question(int id, Date creationDate, int score, String body, int ownerUserId, String lastEditorDisplayName, Date lastEditDate, Date lastActivityDate, int commentCount, String title, ArrayList<String> tags, int answerCount, int favoriteCount) {
        super(id, creationDate, score, body, ownerUserId, lastEditorDisplayName, lastEditDate, lastActivityDate, commentCount);
        this.title = title;
        this.tags = tags;
        this.answerCount = answerCount;
        this.favoriteCount = favoriteCount;
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

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
