package PostProcess;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyFilterProvider implements ConsoleFilterProvider {
    public static MyFilter fi;

    public MyFilterProvider(){

    }

    @NotNull
    @Override
    public Filter[] getDefaultFilters(@NotNull final Project project) {
        MyFilter f = new MyFilter(project);
        return new Filter[]{f};
    }
}
