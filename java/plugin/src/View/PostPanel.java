package View;

import Controller.Controller;
import Model.Post;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.jvnet.ws.wadl.Link;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostPanel extends JPanel {
    JLabel textField;
    JButton detailsButton;
    JButton showCodeButton;
    Post post;
    Controller controller;

    public PostPanel(Post post, Controller controller) {
        this.post = post;
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.controller = controller;

        //Create the text field format, and then the text field.
        textField = new JLabel();
        textField.setText("<html>"+post.getBody()+"</html>");

        detailsButton = new JButton("Details");
        showCodeButton = new JButton("Switch code");

        detailsButton.addActionListener(e -> showDetails());
        showCodeButton.addActionListener(e -> showCode());


        JPanel unitGroup = new JPanel() {
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            public Dimension getPreferredSize() {
                return new Dimension(300,
                        super.getPreferredSize().height);
            }
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };

        unitGroup.setLayout(new BoxLayout(unitGroup,
                BoxLayout.PAGE_AXIS));

        unitGroup.setBorder(BorderFactory.createEmptyBorder(
                0,0,0,5));

        unitGroup.add(textField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,
                BoxLayout.PAGE_AXIS));

        buttonsPanel.add(detailsButton);
        buttonsPanel.add(showCodeButton);

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(unitGroup);
        add(buttonsPanel);
        unitGroup.setAlignmentY(TOP_ALIGNMENT);
        buttonsPanel.setAlignmentY(TOP_ALIGNMENT);
    }

    private void getRelatedPosts() {

    }

    private void showDetails() {
        this.controller.showPostDetails(post);
        //PostDetail postDetail = new PostDetail();
    }

    private void showCode() {

        String output = controller.getConsoleOutput();
        Pattern pattern = Pattern.compile("at (.*)\\(([^<]+):([0-9])+\\)"); // Capture du nom de fichier de la console      ex : at test.test.main(test.java:6)
        Matcher matcher = pattern.matcher(output);
        String fileName = "";
        String callPath = "";
        int line = 0;

        if (matcher.find()){
            callPath = matcher.group(1);
            line = Integer.parseInt(matcher.group(3));
        }

        String[] callTab = callPath.split("\\.");
        for (int i = 0; i < callTab.length - 1; i++) {
            fileName += "/" + callTab[i] ;
        }


        String basePath = controller.getProject().getBasePath();
        String filePath = basePath + "/src" + fileName + ".java";
        SuggestionWindow popup = new SuggestionWindow(null, "Code suggestion", false, "ok", line, filePath);
    }
}
