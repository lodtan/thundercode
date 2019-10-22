package View;

import Controller.Controller;
import Model.Post;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.jvnet.ws.wadl.Link;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PostPanel extends JPanel {
    protected JLabel textField;
    protected JButton detailsButton;
    protected JButton showCodeButton;
    protected Post post;
    protected Controller controller;
    protected JPanel unitGroup;
    protected JPanel buttonsPanel;

    public PostPanel(Post post, Controller controller) {
        //controller.getSearchField().getColor;

        this.post = post;
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.controller = controller;
        initComponent();

    }

    private void getRelatedPosts() {

    }

    private void showDetails() {
        this.controller.showPostDetails(post, true);
        //PostDetail postDetail = new PostDetail();
    }

    private void showCode() {

        String output = controller.getConsoleOutput();
        Pattern pattern = Pattern.compile("at (.*)\\(([^<]+):(\\d*)\\)"); // Capture du nom de fichier de la console      ex : at test.test.main(test.java:6)
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
        SuggestionWindow popup = new SuggestionWindow(null, "Code suggestion", false, getCodeFromPost(), line, filePath);
    }

    private String getCodeFromPost() {
        String body = post.getBody();
        Pattern pattern = Pattern.compile("<div class=\"code-block\">(.*)?<\\/div>"); // Capture du code dans le corps du Post
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public void initComponent (){

        //Create the text field format, and then the text field.
        textField = new JLabel();
        StyleSheet s = null;
        HTMLEditorKit kit;
        try{
            s = loadStyleSheet(this.getClass().getResourceAsStream("/stylesheets/postPanel.css"));

            kit =new HTMLEditorKit();
            kit.setStyleSheet(s);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        textField.setText("<html>" +

                "<body>"+post.getBody()+"</body></html>");
        System.out.println(textField.getText());
        textField.setBorder(BorderFactory.createEmptyBorder(0,10,15,5));
        detailsButton = new JButton("Show details");
        detailsButton.addMouseListener(new HoverButton());
        showCodeButton = new JButton("Switch code");
        showCodeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        detailsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        detailsButton.addActionListener(e -> showDetails());
        showCodeButton.addActionListener(e -> showCode());


        unitGroup = new JPanel() {
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            public Dimension getPreferredSize() {
                return new Dimension(350,
                        super.getPreferredSize().height);
            }
            public Dimension getMaximumSize() {
                return new Dimension(350,
                        super.getPreferredSize().height);
            }
        };

        unitGroup.setLayout(new BoxLayout(unitGroup,
                BoxLayout.PAGE_AXIS));

        unitGroup.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        unitGroup.add(textField);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,
                BoxLayout.PAGE_AXIS));

        buttonsPanel.add(detailsButton);
        buttonsPanel.add(showCodeButton);

        JPanel textButtons = new JPanel();
        textButtons.setLayout(new BoxLayout(textButtons, BoxLayout.LINE_AXIS));

        unitGroup.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        unitGroup.setAlignmentY(TOP_ALIGNMENT);
        buttonsPanel.setAlignmentY(TOP_ALIGNMENT);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        textButtons.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        textButtons.add(unitGroup);
        textButtons.add(buttonsPanel);
        textButtons.setAlignmentX(LEFT_ALIGNMENT);

        add(textButtons);
    }

    public JLabel getTextField() {
        return textField;
    }

    public void setTextField(JLabel textField) {
        this.textField = textField;
    }

    public JButton getDetailsButton() {
        return detailsButton;
    }

    public void setDetailsButton(JButton detailsButton) {
        this.detailsButton = detailsButton;
    }

    public JButton getShowCodeButton() {
        return showCodeButton;
    }

    public void setShowCodeButton(JButton showCodeButton) {
        this.showCodeButton = showCodeButton;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    public JPanel getUnitGroup() {
        return unitGroup;
    }

    public void setUnitGroup(JPanel unitGroup) {
        this.unitGroup = unitGroup;
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public void setButtonsPanel(JPanel buttonsPanel) {
        this.buttonsPanel = buttonsPanel;
    }
    public static StyleSheet loadStyleSheet(InputStream is) throws IOException
    {
        StyleSheet s = new StyleSheet();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        s.loadRules(br, null);
        br.close();

        return s;
    }
}


