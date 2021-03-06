package Model;

import java.util.Date;

/**
 * <b>Answer represents a Post that is an answer to a Question from Stack Overflow.</b>
 * <p>
 * An Answer is defined by all the attributes from a Post and :
 * <ul>
 * <li> parentId : represents the Question linked to the Answer </li>
 * </ul>
 * </p>
 *
 * @see Post
 * @see Question
 */
public class Answer extends Post {
    /**
     * ID of the Question linked to the Answer
     *
     * @see Answer#Answer(int, Date, int, String, Date, int)
     * @see Question
     */
    private int parentId;
    private String title;
    /**
     * Answer Constructor.
     * <p>
     * Returns an Answer object.
     * </p>
     *
     * @param id           The Answer's ID. Inherited from Post.
     * @param creationDate The creation date of the Answer. Inherited from Post.
     * @param score        Score of the Answer. Inherited from Post.
     * @param body         Text body of the Answer. Inherited from Post.
     * @param parentId     ID of the Question linked to the Answer.
     * @see Post#id
     * @see Post#creationDate
     * @see Post#score
     * @see Post#body
     */
    Answer(int id, Date creationDate, int score, String body, int parentId, int userId, String userName) {
        super(id, creationDate, score, body, userId, userName);
        this.parentId = parentId;
    }

    Answer(int id, Date creationDate, int score, String body, int parentId, int userId, String userName, String title) {
        super(id, creationDate, score, body, userId, userName);
        this.parentId = parentId;
        this.title = title.replaceAll("b\\\\'(.*?)'", "$1");

        this.title = this.title.replace("\\n", "");
        this.title = this.title.replace("\\", "");    }

    /**
     * Returns the Answer's Parent ID
     *
     * @return the ID of the Answer's Parent
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Sets the Answer's Parent ID
     *
     * @param parentId The new Answer's Parent ID
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
