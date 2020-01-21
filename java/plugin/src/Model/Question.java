package Model;

import java.util.Date;

/**
 * <b> Question represents a Post that is a Question from Stack Overflow.</b>
 * <p>
 * A Question is defined by all the attributes from a Post and :
 * <ul>
 * <li> title : the title of the Post, is often a question </li>
 * <li> tags : a String with a list of Tags that categorizes the Question </li>
 * <li> viewCount : the number of Views gotten by the Post </li>
 * <li> acceptedAnswerId : the ID of the Answer which has been validated by the owner of the current Question </li>
 * </ul>
 * </p>
 *
 * @see Post
 * @see Answer
 */
public class Question extends Post {

    /**
     * title of the Question
     *
     * @see Question#Question(int, Date, int, String, Date, String, String, int, int)
     */
    private String title;

    /**
     * Tags of the Question
     *
     */
    private String tags;

    /**
     * View count of the Question
     *
     */
    private int viewCount;

    /**
     * Accepted Answer ID of the Question
     *
     */
    private int acceptedAnswerId;

    /**
     * Question Constructor.
     * <p>
     * Returns an Question object.
     * </p>
     *
     * @param id
     *          The Question's ID. Inherited from Post.
     * @param creationDate
     *          The creation date of the Question. Inherited from Post.
     * @param score
     *          Score of the Question. Inherited from Post.
     * @param body
     *          Text body of the Question. Inherited from Post.
     * @param title
     *          Title of the Question.
     * @param tags
     *          Tags linked to the Question.
     * @param viewCount
     *          Number of views gotten by the Question.
     * @param acceptedAnswerId
     *          ID of the Answer that has been accepted by the Question's owner
     *
     * @see Post#id
     * @see Post#creationDate
     * @see Post#score
     * @see Post#body
     */
    public Question(int id, Date creationDate, int score, String body, String title, String tags, int viewCount, int acceptedAnswerId, int userId, String userName) {
        super(id, creationDate, score, body, userId, userName);
        this.title = title.replaceAll("b\\\\'(.*?)'", "$1");

        this.title = this.title.replace("\\n", "");
        this.title = this.title.replace("\\", "");
        this.tags = tags;
        this.viewCount = viewCount;
        this.acceptedAnswerId = acceptedAnswerId;

    }

    /**
     * Returns the Question's Title
     *
     * @return the Question's Title
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the Question's Title
     *
     * @param title
     *          The Question's new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the Question's tags
     *
     * @return the Question's tags
     *
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the Question's Tags
     *
     * @param tags
     *          The Question's new tags, formatted in a String.
     *          Each tag is represented between "<>" and aggregated in the String
     *          Example : <c#><ruby-on-rails><python>
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

}
