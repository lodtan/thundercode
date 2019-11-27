package Model;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b> Post </b>
 * <p>
 * A Post is defined by :
 * <ul>
 * <li> id : unique identifier of a Post </li>
 * <li> creationDate : creation date of the Post </li>
 * <li>
 *     score : the more relevant the Answer, the more upvotes it gets from Stack Overflow Users.
 *     Conversely, Posts can get downvoted. The score is the sum of the upvotes and downvotes.
 * </li>
 * <li> body : body text of the Post </li>
 * </ul>
 * </p>
 * <p>
 *     An abstract class to represent all the Posts from Stack Overflow (Answers and Questions)
 * </p>
 * <p>
 * @see Answer
 * @see Question
 */
public abstract class Post {
    /**
     * ID of the Post
     */
    int id;

    /**
     * Creation Date of the Post
     */
    Date creationDate;

    /**
     * ID of the Post
     */
    int score;

    /**
     * Text body of the Post
     */
    String body;

    /**
     * Name of the User
     */
    private String userName;

    /**
     * ID of the User
     */
    private int userId;


    /**
     * Post Constructor.
     * <p>
     * Returns a Post object.
     * </p>
     *
     * @param id           The Post's ID.
     * @param creationDate The creation date of the Post.
     * @param score        Score of the Post.
     * @param body         Text body of the Post.
     * @return a Post
     */
    public Post(int id, Date creationDate, int score, String body, int userId, String userName) {
        this.id = id;
        this.creationDate = creationDate;
        this.score = score;
        this.body = body.replace("\\n\\n", "");
        this.body = this.body.replace("    ", "&nbsp;&nbsp;");
        this.body = this.body.replace("\\n", "<br>");
        this.body = this.body.replace("\\", "");
        this.body = this.body.replace("<pre><code>", "<div class=\"code-block\"><p>");

        this.body = this.body.replace("</code></pre>", "</p></div>");
        //this.body = this.body.replaceAll("<pre.*?>(.*)?</pre>", ". codeBlock.");

        this.body = this.body.replace("<code>", "<span class=\"lonely-code\">");
        this.body = this.body.replace("</code>", "</span>");
        this.body = this.body.replace("<pre", "<div");

        //this.body = this.body.replaceAll("<a href.*?>", "");
        Pattern pattern = Pattern.compile("(<blockquote>.*?</blockquote>)");
        Matcher matcher = pattern.matcher(this.body);
        String b = "";
        if (matcher.find()) {
            b = matcher.group(1);
        }
        b = b.replaceAll("<p>", "<p class=\"block-p\">");
        this.body = this.body.replaceAll("<blockquote>.*?</blockquote>", b);
        this.userId = userId;
        this.userName = userName;
    }

    /**
     * Returns the Post's ID
     *
     * @return the Post's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Post's ID
     *
     * @param id The Post's new ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the Post's creation date
     *
     * @return the Post's creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the Post's creationDate
     *
     * @param creationDate The Post's new creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the Post's score
     *
     * @return the Post's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the Post's score
     *
     * @param score A new score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the Post's body
     *
     * @return the Post's body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the Post's body
     *
     * @param body The Post's new body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the Post's last activity date
     *
     * @return the Post's last activity date
     */

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}