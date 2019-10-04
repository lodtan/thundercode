import com.intellij.openapi.wm.ToolWindow;
import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class MyToolWindow {

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

    public MyToolWindow(ToolWindow toolWindow) {

        searchButton.addActionListener(e -> search());
        testButton.addActionListener(e -> showPosts());
    }
    public void setContent(String label){
        bodyLabel.setText("ok");
    }
    private void connect(){
        String dir = System.getProperty("idea.plugins.path");
        dir = dir.replaceAll("\\/", "/");
        Properties properties = new Properties();
        String pathToPlugin = dir+"/Plugin/classes/properties/connexion.properties";
        try{

            FileInputStream in = new FileInputStream(pathToPlugin);
            properties.load(in);
            in.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String uri = properties.getProperty("URI");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        connection = new ConnexionBd( uri,  user, password );
    }

    private void showPosts() {
        connect();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(29);
        idList.add(7);

        ArrayList<Answer> resultsList = connection.readNode(idList);

        suggestedPanel.setLayout(new BoxLayout(suggestedPanel, BoxLayout.PAGE_AXIS));
        suggestedPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        for (int i = 0; i < resultsList.size(); i++) {

            // Create a small panel for each result found
            PostPanel postPanel = new PostPanel("title", resultsList.get(i).getBody(), resultsList.get(i).getBody());

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