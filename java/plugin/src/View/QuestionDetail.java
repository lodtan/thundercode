package View;

import Controller.Controller;
import Model.Post;
import Model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    }

    private void setTextPanel() {
        JLabel title = new JLabel("<html>"+ question.getTitle() + "</html>");
        Font f = title.getFont();
        title.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        unitGroup.add(title, 0);
    }

    private void setButtonPanel() {
        JLabel score = new JLabel("Score : "+Integer.toString(question.getScore()));
        buttonsPanel.add(score, 0);

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        JLabel dateCreation = new JLabel("asked "+ date.format(question.getCreationDate()));
        buttonsPanel.add(dateCreation);
    }

    private void setTagsPanel() {
        JPanel tags = createTags();
        tags.setLayout(new GridBagLayout());
        //unitGroup.add(tags);
        add(tags);
    }



    public JPanel createTags() {
        JPanel tagsPanel = new JPanel();
//        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.LINE_AXIS));
//        String tags = question.getTags();
//
//        Pattern pattern = Pattern.compile("\\<(.*?)\\>");
//        Matcher matcher = pattern.matcher(tags);
//
//        while(matcher.find()) {
//            JButton tag = new JButton(matcher.group(1));
//            tagsPanel.add(tag);
//        }
//
//        tagsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        tagsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.WEST;

        String tags = question.getTags();

        Pattern pattern = Pattern.compile("\\<(.*?)\\>");
        Matcher matcher = pattern.matcher(tags);

        while(matcher.find()) {
            String tagName = matcher.group(1);
            JButton tag = new JButton(tagName);
            tagsPanel.add(tag, gbc);
            tag.addActionListener(e -> controller.displayPostsFromTags(tagName));
            tag.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            //System.out.println(matcher.group(1));
        }

        //tagsPanel.setPreferredSize(new Dimension(-1, 1));
        tagsPanel.setMaximumSize(new Dimension(200, 50));
        tagsPanel.validate();
        tagsPanel.setAlignmentX(RIGHT_ALIGNMENT);
        return tagsPanel;
    }
}
