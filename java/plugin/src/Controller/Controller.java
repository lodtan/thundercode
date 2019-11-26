package Controller;

import Model.*;
import View.*;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
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
    private JScrollPane jsp;
    private JTextField searchField;
    private JTextField tagsField;
    private boolean fromSearch;
    private boolean isReady;

    public Controller(Project project) {
        consoleOutput = "";
        answerPanel = new JPanel();
        detailsPanel = new JPanel();
        this.project = project;
        isReady = false;
    }

    public Controller(JTextField searchField, JScrollPane jsp, JTextField tagsField) {
        isReady = false;
        this.jsp = jsp;
        this.searchField = searchField;
        searchField.addKeyListener(new searchEnter());
        this.tagsField = tagsField;
        tagsField.addKeyListener(new searchEnter());

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

        if (!isReady) {
            loadWaitingPanel();
            errorLabel.setText("");
        }
        if (!s.equals("\n")) {
            consoleOutput += s;
        }
        if (s.contains("Process finished with exit code")) {
            System.out.println(s);
            if(s.contains("1")) {
                String errorText = "";
                Pattern pattern = Pattern.compile("Exception in thread \".*\"(.*)"); // Capture du nom de fichier de la console      ex : at test.test.main(test.java:6)
                Matcher matcher = pattern.matcher(consoleOutput);


                if (matcher.find()) {
                    errorText = matcher.group(1);
                }


                if (answerPanel == null)
                    answerPanel = new JPanel();

                answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
                answerPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 0, 2));

                showAnswers(errorText);
                jsp.setViewportView(answerPanel);
                jsp.setBorder(null);

                errorLabel.setText("<html>" + errorText + "</html>");
                errorLabel.setForeground(new Color(255, 107, 104));


                JButton searchButton = (JButton) consoleView.getComponent(3);
                searchButton.addActionListener(e -> search());

                searchField = (JTextField) consoleView.getComponent(2);
                searchField.addKeyListener(new searchEnter());

                tagsField = (JTextField) consoleView.getComponent(4);
                tagsField.addKeyListener(new searchEnter());

                isReady = true;
            }
            else {
                JPanel loadingPanel = new JPanel();
                loadingPanel.add(new JLabel("Your code works fine"));
                jsp.setViewportView(loadingPanel);
                isReady = true;

            }
        }
        return null;
    }


    public String getConsoleOutput() {
        return consoleOutput;
    }

    private void connect() {
        connection = new ConnexionBd();
    }

    private void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAnswers(String errorText) {
        connect();
        System.out.println(errorText);
        ArrayList<Answer> resultsList = connection.searchAnswerByError(errorText);
        for (Answer answer : resultsList) {

            // Create a small panel for each result found
            PostPanel postPanel = new PostPanel(answer, this);
            answerPanel.add(postPanel, 0);
        }
        disconnect();
    }

    public JPanel getAnswerPanel() {
        return answerPanel;
    }

    public JPanel getDetailsPanel() {
        return detailsPanel;
    }

    public void showPostDetails(Post post, boolean fromAnswer) {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        connect();
        Question q;
        if (fromAnswer) {
            q = (Question) connection.getQuestionFromAnswer(post.getId());
            fromSearch = false;
        } else {
            fromSearch = true;
            q = (Question) post;
        }
        QuestionDetail qd = new QuestionDetail(q, this);
        ArrayList<Comment> commentList = connection.getCommentFrom(q.getId(), "Question");
        for (Comment comment : commentList) {
            qd.add(new CommentPanel(comment));
        }
        //qd.setPreferredSize(new Dimension(qd.getPreferredSize().width, qd.getPreferredSize().height));
        detailsPanel.add(qd);

        ArrayList<Answer> listAnswer = connection.getAnswersFromQuestion(q.getId());
        for (Answer answer : listAnswer) {
            AnswerDetail answerDetail = new AnswerDetail(answer, this);
            ArrayList<Comment> commentAnswerList = connection.getCommentFrom(answer.getId(), "Answer");
            System.out.println(commentAnswerList.size());
            for (Comment comment : commentAnswerList) {
                answerDetail.add(new CommentPanel(comment));
            }
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
        if (fromSearch)
            jsp.setViewportView(searchPanel);
        else
            jsp.setViewportView(answerPanel);
    }


    public Project getProject() {
        return project;
    }

    public void search() {
        if (!searchField.getText().equals("") && !searchField.getText().equals("Search by Title or Body")) {
            loadWaitingPanel();

            fromSearch = true;
            connect();
            ArrayList<Question> questionsList;

            if (!tagsField.getText().equals("") && !tagsField.getText().equals("Filter by Tag or search a single Tag")) {
                questionsList = connection.searchNodesByTags(searchField.getText(), tagsField.getText());
            } else {
                questionsList = connection.searchNodes(searchField.getText());
            }

            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
            searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            for (Question q : questionsList) {

                // Create a small panel for each result found
                QuestionDetail postPanel = new QuestionDetail(q, this);
                postPanel.getDetailsButton().setVisible(true);
                postPanel.getDetailsButton().removeActionListener(postPanel.getDetailsButton().getActionListeners()[0]);
                postPanel.getDetailsButton().addActionListener(e -> showPostDetails(q, false));
                postPanel.setPreferredSize(new Dimension(postPanel.getPreferredSize().width, postPanel.getPreferredSize().height));
                searchPanel.add(postPanel);
            }

            jsp.setViewportView(searchPanel);
            disconnect();
        } else {
            if (!tagsField.getText().equals("") && !tagsField.getText().equals("Filter by Tag or search a single Tag")) {
                displayPostsFromTags(tagsField.getText());
                //A FINIR
            }
        }
        jsp.getVerticalScrollBar().setValue(0);

    }

    public void loadWaitingPanel() {
        URL url = getClass().getResource("/img/Spinner-1s-69px.gif");
        JPanel loadingPanel = new JPanel();
        Icon icon = new ImageIcon(url);
        JLabel loadingIcon = new JLabel(icon);
        loadingPanel.add(loadingIcon);
        loadingPanel.add(new JLabel("Your results are loading"));
        jsp.setViewportView(loadingPanel);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public void setSearchField(JTextField searchField) {
        this.searchField = searchField;
    }


    public void displayPostsFromTags(String tagName) {
        connect();
        JPanel relatedTagsPanel = new JPanel();
        relatedTagsPanel.setLayout(new BoxLayout(relatedTagsPanel, BoxLayout.PAGE_AXIS));
        WikiPost wikiTag = connection.getWikiPostFromTag(tagName);
        if (wikiTag != null) {
            WikiPanel wikiPanel = new WikiPanel(wikiTag, tagName, this);
            wikiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            relatedTagsPanel.add(wikiPanel);
        }
        ArrayList<Question> questionList = connection.getQuestionsFromTag(tagName);
        for (Question q : questionList){
            QuestionDetail postPanel = new QuestionDetail(q, this);
            postPanel.getDetailsButton().setVisible(true);
            postPanel.getDetailsButton().removeActionListener(postPanel.getDetailsButton().getActionListeners()[0]);
            postPanel.getDetailsButton().addActionListener(e -> showPostDetails(q, false));
            relatedTagsPanel.add(postPanel);
        }
        jsp.setViewportView(relatedTagsPanel);
        JButton backButton = new JButton("<-");
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backFromTags());
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        relatedTagsPanel.add(backButton, 0);
        disconnect();
    }

    private void backFromTags() {
        if (fromSearch)
            jsp.setViewportView(searchPanel);
        else
            jsp.setViewportView(detailsPanel);
    }


    public class searchEnter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                search();
            }
        }
    }
}
