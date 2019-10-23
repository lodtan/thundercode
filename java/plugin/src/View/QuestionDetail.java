package View;

import Controller.Controller;
import Model.Post;
import Model.Question;
import com.intellij.vcs.log.ui.frame.WrappedFlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.FlowLayout.LEFT;

public class QuestionDetail extends PostPanel {
    Question question;
    public QuestionDetail(Post post, Controller controller) {
        super(post, controller);
        detailsButton.setVisible(false);
        showCodeButton.setVisible(false);
        question = (Question) post;
        setTextPanel();
        setTagsPanel();
        setButtonPanel();
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height));
    }

    private void setTextPanel() {
        //JLabel link = new JLabel("<html><h4>https://stackoverflow.com/questions/</h4></html>"+post.getId());
        //link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel title = new JLabel("<html><h2>"+ question.getTitle() + "</h2></html>");
        title.setToolTipText("Redirect to Stackoverflow");
        Font f = title.getFont();
        title.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        title.setAlignmentX(TOP_ALIGNMENT);
        title.setForeground(new Color(74, 136, 199));
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://stackoverflow.com/questions/"+post.getId()));
                } catch (URISyntaxException | IOException ex) {
                }
            }
        });
        title.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //unitGroup.add(link, 0);
        unitGroup.add(title, 0);
    }

    private void setButtonPanel() {
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        JLabel userLabel = new JLabel("by "+ question.getUserName());
        if (question.getUserId() != 0){
            userLabel.setForeground(new Color(74, 136, 199));
            userLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            userLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://stackoverflow.com/users/"+question.getUserId()));
                    } catch (URISyntaxException | IOException ex) {
                    }
                }
            });
        }
        buttonsPanel.add(userLabel, 0);

        JLabel dateCreation = new JLabel("asked "+ date.format(question.getCreationDate()));
        buttonsPanel.add(dateCreation, 0);
        JLabel score = new JLabel("Score : "+Integer.toString(question.getScore()));
        buttonsPanel.add(score, 0);


    }

    private void setTagsPanel() {
        JPanel tags = createTags();
        add(tags);
    }



    public JPanel createTags() {
        JPanel tagsPanel = new JPanel();
        FlowLayout fl = new WrappedFlowLayout(0,0);
        tagsPanel.setLayout(fl);

        String tags = question.getTags();

        Pattern pattern = Pattern.compile("\\<(.*?)\\>");
        Matcher matcher = pattern.matcher(tags);

        while(matcher.find()) {
            String tagName = matcher.group(1);
            JButton tag = new JButton(tagName);
            tagsPanel.add(tag);
            tag.addActionListener(e -> controller.displayPostsFromTags(tagName));
            tag.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        tagsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        tagsPanel.setMaximumSize(new Dimension(350, 100));
        tagsPanel.setAlignmentX(LEFT_ALIGNMENT);

        return tagsPanel;
    }
}
