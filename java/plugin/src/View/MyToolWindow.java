package View;

import Controller.Controller;
import Model.ConnexionBd;
import Model.Question;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.util.ArrayList;

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

    private Controller controller;

    public MyToolWindow(ToolWindow toolWindow) {
        detailsPanel.setBorder(null);

        controller = new Controller(searchField, detailsPanel);
        searchButton.addActionListener(e -> controller.search());
        //answerPanel.setVisible(true);
        //detailsPanel.setVisible(false);

        //errorLabel.addPropertyChangeListener(e -> showAnswers());

        //testButton.addActionListener(e -> showAnswers());
    }
    public void setContent(String label){
        bodyLabel.setText("ok");
    }


    public JPanel getContent() {
        return myToolWindowContent;
    }

    public void setErrorLabel(String s){
        errorLabel.setText(s);
    }

}