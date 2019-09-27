import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
            //new FileModif(project.getBasePath());

            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Sample Calendar");
            //MyToolWindowFactory mf = new MyToolWindowFactory(toolWindow);
            MyToolWindowFactory.changeText(project, toolWindow, consoleOutput);



        }
        //System.out.println(consoleOutput);

        return null;
    }


    public String getConsoleOutput(){
        return consoleOutput;
    }
}
