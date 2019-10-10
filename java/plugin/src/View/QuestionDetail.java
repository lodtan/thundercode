package View;

import Controller.Controller;
import Model.Post;
import Model.Question;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class QuestionDetail extends PostPanel {
    Question question;
    public QuestionDetail(Post post, Controller controller) {
        super(post, controller);
        detailsButton.setVisible(false);
        showCodeButton.setVisible(false);
        question = (Question) post;
        setTextPanel();
        setButtonPanel();


    }

    private void setTextPanel() {
        JLabel title = new JLabel("<html>"+ question.getTitle() + "</html>");
        Font f = title.getFont();
        title.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        JLabel tags = new JLabel(question.getTags());
        tags.setForeground(Color.blue);
        unitGroup.add(title, 0);
        unitGroup.add(tags);
    }

    private void setButtonPanel() {
        JLabel score = new JLabel("Score : "+Integer.toString(question.getScore()));
        buttonsPanel.add(score, 0);

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        JLabel dateCreation = new JLabel("asked "+ date.format(question.getCreationDate()));
        buttonsPanel.add(dateCreation);
    }
}
