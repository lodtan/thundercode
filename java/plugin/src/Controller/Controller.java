package Controller;

import Model.*;
import View.AnswerDetail;
import View.PostPanel;
import View.QuestionDetail;
import View.WikiPanel;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javafx.application.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Filter {
    private String consoleOutput;
    private Project project;
    private ConnexionBd connection;
    private JPanel answerPanel;
    private JPanel detailsPanel;
    private JPanel searchPanel;
    private JPanel relatedTagsPanel;
    private JPanel loadingPanel;
    private JScrollPane jsp;
    private JTextField searchField;
    private boolean fromSearch;
    private boolean isReady;

    public Controller(Project project) {
        consoleOutput = "";
        answerPanel = new JPanel();
        detailsPanel = new JPanel();
        this.project = project;
        isReady = false;
    }

    public Controller(JTextField searchField, JScrollPane jsp){
        isReady = false;
        this.jsp = jsp;
        this.searchField = searchField;
        answerPanel = new JPanel();
        detailsPanel = new JPanel();
    }

    @Nullable
    @Override
    public Result applyFilter(@NotNull String s, int endPoint) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ThunderCode");
        ContentManager contentManager = toolWindow.getContentManager();
        Content content2 = contentManager.findContent("");
        JPanel consoleView = (JPanel) content2.getComponent();
        JTabbedPane tbp = (JTabbedPane) consoleView.getComponent(0);
        JPanel jp = (JPanel) tbp.getComponent(0);
        jsp = (JScrollPane) jp.getComponent(0);
        JLabel errorLabel = (JLabel) consoleView.getComponent(1);

        if(!isReady) {
/*            String dir = System.getProperty("idea.plugins.path");
            dir = dir.replace("\\", "/");
            String pathToPlugin = dir+"Plugin/classes/img/Spinner-1s-200px.gif";
            System.out.println(pathToPlugin);*/
            URL url = getClass().getResource("/img/Spinner-1s-69px.gif");
            //URL url = getClass().getResource(pathToPlugin);
            loadingPanel = new JPanel();
            Icon icon = new ImageIcon(url);
            JLabel loadingIcon = new JLabel(icon);
            loadingPanel.add(loadingIcon);
            loadingPanel.add(new JLabel("Your results are loading"));

            //searchPanel = new JPanel();
            jsp.setViewportView(loadingPanel);
            errorLabel.setText("");
        }
        if(!s.equals("\n")) {
            //System.out.println(s);
            consoleOutput += s;
        }
        if(s.contains("Process finished with exit code")){
            //new PostProcess.Process.FileModif(project.getBasePath());
            String errorText="";
            Pattern pattern = Pattern.compile("Exception in thread \".*\"(.*)"); // Capture du nom de fichier de la console      ex : at test.test.main(test.java:6)
            Matcher matcher = pattern.matcher(consoleOutput);
            String fileName = "";
            String callPath = "";
            int line = 0;

            if (matcher.find()){
                errorText = matcher.group(1);
            }


            //answerPanel = (JPanel) jp.getComponent(0);
            if(answerPanel == null)
                answerPanel = new JPanel();

            answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
            answerPanel.setBorder(BorderFactory.createEmptyBorder(5,2,0,2));

            showAnswers();
            //jsp.add(answerPanel);
            jsp.setViewportView(answerPanel);
            jsp.setBorder(null);

            errorLabel.setText("<html>"+errorText+"</html>");
            errorLabel.setForeground(new Color(255, 107, 104));



            JButton searchButton = (JButton) consoleView.getComponent(3);
            searchButton.addActionListener(e -> search());

            searchField = (JTextField) consoleView.getComponent(2);
            isReady = true;
        }
        return null;
    }


    public String getConsoleOutput(){
        return consoleOutput;
    }

    public void connect(){
        connection = new ConnexionBd();
    }

    private void disconnect() {
        try {
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAnswers() {
        connect();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(51);
        idList.add(52);

        ArrayList<Answer> resultsList = connection.readNode(idList);


        for (int i = 0; i < resultsList.size(); i++) {

            // Create a small panel for each result found
            PostPanel postPanel = new PostPanel(resultsList.get(i), this);
            //postPanelList.add(postPanel);
            //answerPanel.add(postPanel, 0);
            answerPanel.add(postPanel, 0);
        }
        disconnect();
    }
    public JPanel getAnswerPanel(){
        return answerPanel;
    }

    public JPanel getDetailsPanel(){
        return detailsPanel;
    }

    public void showPostDetails(Post post, boolean fromAnswer) {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        connect();
        Question q;
        if (fromAnswer){
            q = (Question) connection.getQuestionFromAnswer(post.getId());
            fromSearch = false;
        }
        else{
            fromSearch = true;
            q = (Question) post;
        }
        QuestionDetail qd = new QuestionDetail(q, this);
        detailsPanel.add(qd);

        ArrayList<Answer> listAnswer = connection.getAnswersFromQuestion(q.getId());
        for (int i =0; i <listAnswer.size(); i++) {
            AnswerDetail answerDetail = new AnswerDetail(listAnswer.get(i), this);
            detailsPanel.add(answerDetail);
        }
        disconnect();


        JButton backButton = new JButton("<-");
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backDetails());
        backButton.setAlignmentX(Component.TOP_ALIGNMENT);
        detailsPanel.add(backButton, 0);
        jsp.setViewportView(detailsPanel);
    }

    private void backDetails() {
        if(fromSearch)
            jsp.setViewportView(searchPanel);
        else
            jsp.setViewportView(answerPanel);
    }


    public Project getProject() {
        return project;
    }

    public void search() {
        if (searchField.getText() != null || !searchField.getText().equals("")) {
            URL url = getClass().getResource("/img/Spinner-1s-69px.gif");
            //URL url = getClass().getResource(pathToPlugin);
            loadingPanel = new JPanel();
            Icon icon = new ImageIcon(url);
            JLabel loadingIcon = new JLabel(icon);
            loadingPanel.add(loadingIcon);
            loadingPanel.add(new JLabel("Your results are loading"));

            //searchPanel = new JPanel();
            jsp.setViewportView(loadingPanel);
            fromSearch = true;
            connect();
            ArrayList<Question> questionsList = connection.searchNodes(searchField.getText());
            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
            searchPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            System.out.println(questionsList.size());
            for (int i = 0; i < questionsList.size(); i++) {

                // Create a small panel for each result found
                Question q = questionsList.get(i);
                QuestionDetail postPanel = new QuestionDetail(q, this);
                postPanel.getDetailsButton().setVisible(true);
                postPanel.getDetailsButton().removeActionListener(postPanel.getDetailsButton().getActionListeners()[0]);
                postPanel.getDetailsButton().addActionListener(e -> showPostDetails(q, false));
                //postPanelList.add(postPanel);
                //answerPanel.add(postPanel, 0);
                searchPanel.add(postPanel);
                System.out.println(questionsList.get(i).getBody());
            }

            jsp.setViewportView(searchPanel);
            disconnect();
        }
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public void setSearchField(JTextField searchField) {
        this.searchField = searchField;
    }


    public void displayPostsFromTags(String tagName) {
        connect();
        relatedTagsPanel = new JPanel();
        relatedTagsPanel.setLayout(new BoxLayout(relatedTagsPanel, BoxLayout.PAGE_AXIS));

        WikiPost wikiTag = connection.getWikiPostFromTag(tagName);
        WikiPanel wikiPanel = new WikiPanel(wikiTag, tagName,  this);
        wikiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        relatedTagsPanel.add(wikiPanel);
        jsp.setViewportView(relatedTagsPanel);
        JButton backButton = new JButton("<-");
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backFromTags());
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        relatedTagsPanel.add(backButton, 0);
        disconnect();
    }

    private void backFromTags() {
        if(fromSearch)
            jsp.setViewportView(searchPanel);
        else
            jsp.setViewportView(detailsPanel);
    }
}
