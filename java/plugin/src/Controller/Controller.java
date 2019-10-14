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
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import Process.Traitement;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    private JScrollPane jsp;
    private JTextField searchField;

    public Controller(Project project) {
        consoleOutput = "";
        answerPanel = new JPanel();
        detailsPanel = new JPanel();
        this.project = project;
    }

    public Controller(JTextField searchField, JScrollPane jsp){
        this.jsp = jsp;
        this.searchField = searchField;
        answerPanel = new JPanel();
        detailsPanel = new JPanel();
    }
    @Nullable
    @Override
    public Result applyFilter(@NotNull String s, int endPoint) {
        if(!s.equals("\n")) {
            //System.out.println(s);
            consoleOutput += s;
        }
        if(s.contains("Process finished with exit code")){
            new Traitement(consoleOutput);
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

            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ThunderCode");

            ContentManager contentManager = toolWindow.getContentManager();
            Content content2 = contentManager.findContent("");
            JPanel consoleView = (JPanel) content2.getComponent();



            JTabbedPane tbp = (JTabbedPane) consoleView.getComponent(0);
            JPanel jp = (JPanel) tbp.getComponent(0);
            jsp = (JScrollPane) jp.getComponent(0);

            //answerPanel = (JPanel) jp.getComponent(0);
            if(answerPanel == null)
                answerPanel = new JPanel();

            answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
            answerPanel.setBorder(BorderFactory.createEmptyBorder(5,2,0,2));

            showAnswers();
            //jsp.add(answerPanel);
            jsp.setViewportView(answerPanel);
            jsp.setBorder(null);

            JLabel errorLabel = (JLabel) consoleView.getComponent(1);
            errorLabel.setText("<html>"+errorText+"</html>");
            errorLabel.setForeground(new Color(255, 107, 104));



            JButton searchButton = (JButton) consoleView.getComponent(3);
            searchButton.addActionListener(e -> search());

            searchField = (JTextField) consoleView.getComponent(2);

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
        idList.add(5261);
        idList.add(5599);

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

    public void showPostDetails(Post post) {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        connect();
        Question q = (Question) connection.getQuestionFromAnswer(post.getId());
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
        jsp.setViewportView(answerPanel);
    }


    public Project getProject() {
        return project;
    }

    public void search() {
        if (searchField.getText() != null || !searchField.getText().equals("")) {
            connect();
            ArrayList<Question> questionsList = connection.searchNodes(searchField.getText());
            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
            searchPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            System.out.println(questionsList.size());
            for (int i = 0; i < questionsList.size(); i++) {

                // Create a small panel for each result found
                QuestionDetail postPanel = new QuestionDetail(questionsList.get(i), this);
                //postPanelList.add(postPanel);
                //answerPanel.add(postPanel, 0);
                searchPanel.add(postPanel, 0);

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
        WikiPost wikiTag = connection.getWikiPostFromTag(tagName);
        WikiPanel wikiPanel = new WikiPanel(wikiTag, tagName,  this);
        relatedTagsPanel.add(wikiPanel);
        jsp.setViewportView(relatedTagsPanel);
        JButton backButton = new JButton("<-");
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> jsp.setViewportView(detailsPanel));
        backButton.setAlignmentX(Component.TOP_ALIGNMENT);
        relatedTagsPanel.add(backButton, 0);
        disconnect();
    }
}
