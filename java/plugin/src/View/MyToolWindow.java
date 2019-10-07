package View;

import Model.Answer;
import Model.ConnexionBd;
import Model.Post;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.util.ArrayList;

public class MyToolWindow extends JComponent {

    private JPanel myToolWindowContent;
    private JTextField searchField;
    private JTabbedPane tabbedPane;
    private JButton searchButton;
    private JLabel bodyLabel;
    private JPanel suggestedPanel;
    private JPanel trendsPanel;
    private JButton testButton;
    private JLabel errorLabel;
    private ConnexionBd connection;
    private ArrayList<PostPanel> postPanelList;

    public MyToolWindow(ToolWindow toolWindow) {
        postPanelList = new ArrayList<PostPanel>();
        //errorLabel.addPropertyChangeListener(e -> showAnswers());
        searchButton.addActionListener(e -> search());
        testButton.addActionListener(e -> showAnswers());
    }
    public void setContent(String label){
        bodyLabel.setText("ok");
    }
    public void connect(){
        connection = new ConnexionBd();
    }

    public void showAnswers() {
        connect();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(29);

        ArrayList<Answer> resultsList = connection.readNode(idList);

        suggestedPanel.setLayout(new BoxLayout(suggestedPanel, BoxLayout.PAGE_AXIS));
        suggestedPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        for (int i = 0; i < resultsList.size(); i++) {

            // Create a small panel for each result found
            PostPanel postPanel = new PostPanel(resultsList.get(i));
            postPanelList.add(postPanel);
            suggestedPanel.add(postPanel, 0);

        }

        disconnect();
    }

    private void search() {
        connect();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(Integer.parseInt(searchField.getText()));
        ArrayList<Answer> resultsList = connection.readNode(idList);
        bodyLabel.setText(resultsList.get(0).getBody());
        disconnect();
    }

    private void disconnect() {
        try {
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    public void setErrorLabel(String s){
        errorLabel.setText(s);
    }
}