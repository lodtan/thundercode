package View;

import Model.Answer;
import Model.ConnexionBd;
import Model.Post;
import com.intellij.openapi.wm.ToolWindow;

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
    private JPanel AnswerPanel;
    private JPanel DetailPanel;
    private ArrayList<PostPanel> postPanelList;

    public MyToolWindow(ToolWindow toolWindow) {
        postPanelList = new ArrayList<PostPanel>();
        AnswerPanel.setVisible(true);
        DetailPanel.setVisible(false);
        //errorLabel.addPropertyChangeListener(e -> showAnswers());
        //searchButton.addActionListener(e -> search());
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