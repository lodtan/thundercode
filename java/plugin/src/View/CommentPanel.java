package View;

import Model.Comment;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CommentPanel extends JPanel {
    public CommentPanel(Comment comment){
        StyleSheet s;
        HTMLEditorKit kit;
        try {
            s = loadStyleSheet(this.getClass().getResourceAsStream("/stylesheets/postPanel.css"));

            kit = new HTMLEditorKit();

            kit.setStyleSheet(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JTextPane textField = new JTextPane();
        textField.setContentType("text/html"); // let the text pane know this is what you want
        textField.setEditable(false); // as before
        textField.setBackground(null); // this is the same as a JLabel
        textField.setBorder(null); // remove the border
        DateFormat date = new SimpleDateFormat("MMM dd '\'YY", Locale.ENGLISH);

        textField.setText("<html><div ><p class=\"comment\">"+comment.getScore()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+comment.getText()+" - <a href=\"https://stackoverflow.com/users/"+
                comment.getIdUser()+"\" class=\"userColor\">"+ comment.getUserName()+"</a> " +
                date.format(comment.getCommentDate())+"</p></div></html>");

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
        add(textField);
        setLayout(new BoxLayout(this,
                BoxLayout.PAGE_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(0, -70, 10, 0));
    }

    private static StyleSheet loadStyleSheet(InputStream is) throws IOException {
        StyleSheet s = new StyleSheet();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        s.loadRules(br, null);
        br.close();

        return s;
    }
}
