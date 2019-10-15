package View;

import Controller.Controller;
import Model.Answer;
import Model.Post;

import javax.swing.*;

/**
 * <b> Answer Detail is the interface that will show the Answer in detail </b>
 * <p> It inherits its properties from PostPanel </p>
 * <p>
 * An Answer is defined by all the attributes from a PostPanel and :
 * <ul>
 * <li> answer : the Answer that will be detailed in the View </li>
 * </ul>
 * </p>
 *
 * @see PostPanel
 *
 * @author Thomas BRES
 * @author Elodie TAN
 */
public class AnswerDetail extends PostPanel {
    /**
     * Answer that will be detailed in the View
     *
     * @see Answer
     */
    private Answer answer;

    /**
     * AnswerDetail Constructor.
     * <p>
     * Returns an AnswerDetail object (interface).
     * </p>
     *
     * @param post
     *          The post to show in AnswerDetail.
     * @param controller
     *          The controller that is used to control the View and Model.
     *
     * @see PostPanel
     * @see Post
     * @see Controller
     */
    public AnswerDetail(Post post, Controller controller) {
        super(post, controller);
        answer = (Answer) post;  // Cast a post into an Answer because the constructor is inherited from PostPanel
        detailsButton.setVisible(false);  // detailsButton from PostPanel
        //showCodeButton.setVisible(false);
        JLabel score = new JLabel("Score : "+Integer.toString(answer.getScore()));
        buttonsPanel.add(score, 0);
    }

}
