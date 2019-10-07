package Controller;

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

public class Controller implements Filter {
    private String consoleOutput;
    Project project;
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
            JLabel j = (JLabel) consoleView.getComponent(2);
            JTabbedPane tbp = (JTabbedPane) consoleView.getComponent(0);
            //JTabbedPane tbp = (JTabbedPane) mp.getComponent(0);
            JPanel sp = (JPanel) tbp.getComponent(0);
            System.out.println(sp.getComponentCount());
            j.setText("okkk");
            sp.add(j,0);
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
}
