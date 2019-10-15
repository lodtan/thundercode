import com.intellij.execution.actions.ConsoleActionsPostProcessor;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class PostProcessorLoading extends ConsoleActionsPostProcessor {
    public AnAction[] postProcess(@NotNull ConsoleView console, @NotNull AnAction[] actions) {
        Component j = console.getComponent();
        System.out.println("sdds");
        ArrayList<AnAction> anActions = new ArrayList<>();
        return anActions.toArray(new AnAction[anActions.size()]);
    }

}
