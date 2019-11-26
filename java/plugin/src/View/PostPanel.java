package View;

import Controller.Controller;
import Model.Post;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PostPanel extends JPanel {
    private JTextPane textField;
    JButton detailsButton;
    JButton showCodeButton;
    protected Post post;
    Controller controller;
    JPanel unitGroup;
    JPanel buttonsPanel;

    public PostPanel(Post post, Controller controller) {
        this.post = post;
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.controller = controller;
        initComponent();

    }


    private void showDetails() {
        this.controller.showPostDetails(post, true);
    }

    private void showCode() {

        String output = controller.getConsoleOutput();
        Pattern pattern = Pattern.compile("at (.*)\\(([^<]+):(\\d*)\\)"); // Capture du nom de fichier de la console      ex : at test.test.main(test.java:6)
        Matcher matcher = pattern.matcher(output);
        String fileName = "";
        String callPath = "";
        int line = 0;

        if (matcher.find()) {
            callPath = matcher.group(1);
            line = Integer.parseInt(matcher.group(3));
        }

        String[] callTab = callPath.split("\\.");
        for (int i = 0; i < callTab.length - 1; i++) {
            fileName += "/" + callTab[i];
        }


        String basePath = controller.getProject().getBasePath();
        String filePath = basePath + "/src" + fileName + ".java";
        SuggestionWindow popup = new SuggestionWindow(null, "Code suggestion", false, getCodeFromPost(), line, filePath);
    }

    private String getCodeFromPost() {
        String body = post.getBody();
        Pattern pattern = Pattern.compile("<div class=\"code-block\">(.*)?</div>"); // Capture du code dans le corps du Post
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private void initComponent() {
        StyleSheet s;
        HTMLEditorKit kit;
        try {
            s = loadStyleSheet(this.getClass().getResourceAsStream("/stylesheets/postPanel.css"));

            kit = new HTMLEditorKit();

            kit.setStyleSheet(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Create the text field format, and then the text field.
        textField = new JTextPane();
        textField.setContentType("text/html"); // let the text pane know this is what you want
        textField.setEditable(false); // as before
        textField.setBackground(null); // this is the same as a JLabel
        textField.setBorder(null); // remove the border


        textField.setText("<html>" +
                "<body>" + post.getBody() + "</body></html>");
        textField.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 5));
        textField.setAlignmentX(TOP_ALIGNMENT);
        textField.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
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
        if(post.getBody().contains("code-block")) {
            buttonsPanel.add(showCodeButton);
        }
        JPanel textButtons = new JPanel();
        textButtons.setLayout(new BoxLayout(textButtons, BoxLayout.LINE_AXIS));

        unitGroup.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        unitGroup.setAlignmentY(TOP_ALIGNMENT);
        buttonsPanel.setAlignmentY(TOP_ALIGNMENT);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        textButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        textButtons.add(unitGroup);
        textButtons.add(buttonsPanel);
        textButtons.setAlignmentX(LEFT_ALIGNMENT);

        add(textButtons);
    }

    public JTextPane getTextField() {
        return textField;
    }

    public void setTextField(JTextPane textField) {
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

    private static StyleSheet loadStyleSheet(InputStream is) throws IOException {
        StyleSheet s = new StyleSheet();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        s.loadRules(br, null);
        br.close();

        return s;
    }
}


