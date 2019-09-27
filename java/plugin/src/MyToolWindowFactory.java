
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alexey.Chursin
 * Date: Aug 25, 2010
 * Time: 2:09:00 PM
 */
public class MyToolWindowFactory implements ToolWindowFactory {

    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public static void changeText(Project project, ToolWindow toolWindow, String consoleOutput){
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
        myToolWindow.setConsoleText(consoleOutput);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        System.out.println("1:"+toolWindow.getContentManager().getContentCount());

        toolWindow.getContentManager().addContent(content);
        //toolWindow.getContentManager().getContent(0).setComponent(content.getComponent());
        System.out.println("2:"+toolWindow.getContentManager().getContentCount());

    }
}