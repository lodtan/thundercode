package View;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class MyToolWindowFactory implements ToolWindowFactory {

    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MyToolWindow myToolWindow = null;
        myToolWindow = new MyToolWindow(toolWindow, project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        content.setDisplayName("");
        toolWindow.getContentManager().addContent(content);
    }
}