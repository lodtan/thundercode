import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class MyFilter implements Filter {
    private String consoleOutput;
    Project project;
    public MyFilter(Project project) {
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
            //new PostProcess.FileModif(project.getBasePath());

            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ThunderCode");
            System.out.println(toolWindow.getComponent().getComponentCount());
            Content c = toolWindow.getContentManager().findContent("errorLabel");
            c.toString();
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
