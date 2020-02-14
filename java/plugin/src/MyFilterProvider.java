import Controller.Controller;
import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MyFilterProvider implements ConsoleFilterProvider {

    public MyFilterProvider() {

    }

    @NotNull
    @Override
    public Filter[] getDefaultFilters(@NotNull final Project project) {
        Controller f = null;
        f = new Controller(project);

        return new Filter[]{f};
    }
}
