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
    protected int parentId;

    /**
     * Answer Constructor.
     * <p>
     * Returns an Answer object.
     * </p>
     *
     * @param id
     *          The Answer's ID. Inherited from Post.
     * @param creationDate
     *          The creation date of the Answer. Inherited from Post.
     * @param score
     *          Score of the Answer. Inherited from Post.
     * @param body
     *          Text body of the Answer. Inherited from Post.
     * @param parentId
     *          ID of the Question linked to the Answer.
     *
     * @see Post#id
     * @see Post#creationDate
     * @see Post#score
     * @see Post#body
     */
    public Answer(int id, Date creationDate, int score, String body, int parentId) {
        super(id, creationDate, score, body);
        this.parentId = parentId;
    }

    /**
     * Returns the Answer's Parent ID
     *
     * @return the ID of the Answer's Parent
     *
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Sets the Answer's Parent ID
     *
     * @param parentId
     *          The new Answer's Parent ID
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
