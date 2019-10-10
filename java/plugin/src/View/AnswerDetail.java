package View;

import Controller.Controller;
import Model.Answer;
import Model.Post;

import javax.swing.*;

public class AnswerDetail extends PostPanel {
    private Answer answer;

    public AnswerDetail(Post post, Controller controller) {
        super(post, controller);
        answer = (Answer) post;
        detailsButton.setVisible(false);
        //showCodeButton.setVisible(false);
        JLabel score = new JLabel(Integer.toString(answer.getScore()));
        buttonsPanel.add(score, 0);
    }

}
