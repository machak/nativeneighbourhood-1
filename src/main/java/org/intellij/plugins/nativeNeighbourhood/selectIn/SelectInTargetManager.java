/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 03.08.2005
 * Time: 00:30:37
 */
package org.intellij.plugins.nativeNeighbourhood.selectIn;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ide.SelectInManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author frb
 */
public class SelectInTargetManager implements ApplicationComponent, ProjectManagerListener {

    private SelectInFileManagerTarget selectInFileManagerTarget = new SelectInFileManagerTarget();

    public static SelectInTargetManager getInstance() {
        Application application = ApplicationManager.getApplication();
        return application.getComponent(SelectInTargetManager.class);
    }

    @NotNull
    public String getComponentName() {
        return SelectInTargetManager.class.getName();
    }

    public void initComponent() {
        ProjectManager.getInstance().addProjectManagerListener(this);
    }

    public void disposeComponent() {
        ProjectManager.getInstance().removeProjectManagerListener(this);
    }

    public void projectOpened(Project pProject) {
        SelectInManager.getInstance(pProject).addTarget(selectInFileManagerTarget);
    }

    public boolean canCloseProject(Project pProject) {
        return true;
    }

    public void projectClosed(Project pProject) {
        SelectInManager.getInstance(pProject).removeTarget(selectInFileManagerTarget);
    }

    public void projectClosing(Project pProject) {}
}
