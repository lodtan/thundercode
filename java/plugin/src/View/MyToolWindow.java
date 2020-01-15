package View;

import Controller.Controller;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MyToolWindow extends JPanel {

    private JPanel myToolWindowContent;
    private JTextField searchField;
    private JTabbedPane tabbedPane;
    private JButton searchButton;
    private JLabel bodyLabel;
    private JPanel trendsPanel;
    private JLabel errorLabel;
    private JPanel suggestedPanel;
    private JScrollPane detailsPanel;
    private JTextField tagsField;
    private JScrollPane trendsDetails;
    private Controller controller;

    MyToolWindow(ToolWindow toolWindow) {
        searchField.setForeground(Color.GRAY);
        searchField.setText("Search by Title or Body");
        tagsField.setForeground(Color.GRAY);
        tagsField.setText("Filter by Tag or search a single Tag");
        detailsPanel.setBorder(null);
        detailsPanel.getVerticalScrollBar().setUnitIncrement(16);

        trendsDetails.setBorder(null);
        trendsDetails.getVerticalScrollBar().setUnitIncrement(16);

        controller = new Controller(searchField, detailsPanel, tagsField, trendsDetails);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> controller.search());
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search by Title or Body")) {
                    searchField.setText("");
                    searchField.setForeground(UIManager.getColor("JTextField"));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search by Title or Body");
                }
            }
        });

        tagsField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tagsField.getText().equals("Filter by Tag or search a single Tag")) {
                    tagsField.setText("");
                    tagsField.setForeground(UIManager.getColor("JTextField"));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tagsField.getText().isEmpty()) {
                    tagsField.setForeground(Color.GRAY);
                    tagsField.setText("Filter by Tag or search a single Tag");
                }
            }
        });

    }

    public void setContent(String label) {
        bodyLabel.setText("ok");
    }


    public JPanel getContent() {
        return myToolWindowContent;
    }

    public void setErrorLabel(String s) {
        errorLabel.setText(s);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}