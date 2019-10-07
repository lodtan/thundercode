package Controller;

import Model.Answer;
import Model.ConnexionBd;
import View.PostPanel;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import Process.Traitement;
import javax.swing.*;
import java.util.ArrayList;

public class Controller implements Filter {
    private String consoleOutput;
    Project project;
    private ConnexionBd connection;
    private JPanel answerPanel;
    public Controller(Project project) {
        consoleOutput = "";
        this.project = project;
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

            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ThunderCode");
            /*
            MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            myToolWindow.showAnswers();
            myToolWindow.setErrorLabel("isjdjidsds");
            Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
            content.setDisplayName("");
            toolWindow.getContentManager().addContent(content);


            */
            System.out.println(toolWindow.getComponent().getComponentCount());
            ContentManager contentManager = toolWindow.getContentManager();
            Content content2 = contentManager.findContent("");
            JPanel consoleView = (JPanel) content2.getComponent();
            //JLabel j = (JLabel) consoleView.getComponent(2);
            JTabbedPane tbp = (JTabbedPane) consoleView.getComponent(0);
            //JTabbedPane tbp = (JTabbedPane) mp.getComponent(0);
            JPanel jp = (JPanel) tbp.getComponent(0);
            answerPanel = (JPanel) jp.getComponent(0);
            showAnswers();
            /*

            MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
            myToolWindow.setErrorLabel("sddssddssd");
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
*/


            //MyToolWindowFactory mf = new MyToolWindowFactory(toolWindow);
            //MyToolWindowFactory.changeText(project, toolWindow, consoleOutput);
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
        idList.add(29);

        ArrayList<Answer> resultsList = connection.readNode(idList);

        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.PAGE_AXIS));
        answerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        for (int i = 0; i < resultsList.size(); i++) {

            // Create a small panel for each result found
            PostPanel postPanel = new PostPanel(resultsList.get(i));
            //postPanelList.add(postPanel);
            answerPanel.add(postPanel, 0);

        }

        disconnect();
    }

/*    private void search() {
        connect();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(Integer.parseInt(searchField.getText()));
        ArrayList<Answer> resultsList = connection.readNode(idList);
        bodyLabel.setText(resultsList.get(0).getBody());
        disconnect();
    }*/
}
