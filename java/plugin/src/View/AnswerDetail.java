package View;

import Controller.Controller;
import Model.Answer;
import Model.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
 * @author Thomas BRES
 * @author Elodie TAN
 * @see PostPanel
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
     * @param post       The post to show in AnswerDetail.
     * @param controller The controller that is used to control the View and Model.
     * @see PostPanel
     * @see Post
     * @see Controller
     */
    public AnswerDetail(Post post, Controller controller) {
        super(post, controller);
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        JLabel userLabel = new JLabel("<html><div class=\"userName\">by <span>" + post.getUserName() + "</span></div></html>");
        if (post.getUserId() != 0) {
            //userLabel.setForeground(new Color(74, 136, 199));
            userLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            userLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://stackoverflow.com/users/" + post.getUserId()));
                    } catch (URISyntaxException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        buttonsPanel.add(userLabel, 0);
        JLabel dateCreation = new JLabel("answered " + date.format(post.getCreationDate()));
        buttonsPanel.add(dateCreation, 0);
        answer = (Answer) post;  // Cast a post into an Answer because the constructor is inherited from PostPanel
        detailsButton.setVisible(false);  // detailsButton from PostPanel
        //showCodeButton.setVisible(false);
        JLabel score = new JLabel("Score : " + answer.getScore());
        buttonsPanel.add(score, 0);
    }

}
