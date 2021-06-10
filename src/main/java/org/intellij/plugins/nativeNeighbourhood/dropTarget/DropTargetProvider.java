/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 25.08.2005
 * Time: 00:17:34
 */
package org.intellij.plugins.nativeNeighbourhood.dropTarget;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.WindowManager;
import org.intellij.plugins.nativeNeighbourhood.Configurator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * @author frb
 */
public class DropTargetProvider implements ProjectComponent, FileOpenListener {

    final static Logger log = Logger.getInstance(Configurator.class.getName());

    private Project project;

    private DropTargetPanel dropTarget;

    public DropTargetProvider(Project pProject) {
        project = pProject;
    }

    /**
     * Invoked when the project corresponding to this component instance is opened.<p>
     * Note that components may be created for even unopened projects and this method can be never
     * invoked for a particular component intance (for example for default project).
     */
    public void projectOpened() {
        if (dropTarget == null) {
            Frame frame = getFrame();
            String tTitleFragment = project.getName();
            if (frame.getTitle().contains(tTitleFragment)) {
//                Component priorGlassPane = frame.getGlassPane();
//                if (priorGlassPane instanceof DropTargetPanel) {
//                    log.debug("Frame has already assigned a DropTargetPanel");
//                } else {
                    dropTarget = new DropTargetPanel(frame, project, this, null/*priorGlassPane*/);
	            // probably not necessary, "Setting of glass pane for IdeFrame is prohibited"
//                    ((JFrame)frame).setGlassPane(dropTarget);
//                }
            } else {
                log.debug("Unable to determine frame for project " + project.getName());
            }
        }
    }

    private Frame getFrame() {
        Frame tResult = null;
        Window tWindow = WindowManager.getInstance().suggestParentWindow(project);
        if (tWindow instanceof Frame) {
            tResult = (Frame)tWindow;
        } else { // fallback
            tResult = JOptionPane.getRootFrame();
        }
        return tResult;
    }

    /**
     * Invoked when the project corresponding to this component instance is closed.<p>
     * Note that components may be created for even unopened projects and this method can be never
     * invoked for a particular component intance (for example for default project).
     */
    public void projectClosed() {
    }

    /**
     * Unique name of this component. If there is another component with the same name or
     * name is null internal assertion will occur.
     *
     * @return the name of this component
     */
    @NotNull
    public String getComponentName() {
        return "org.intellij.plugins.nativeNeighbourhood.dropTarget.DropTargetProvider";
    }

    /**
     * Component should do initialization and communication with another components in this method.
     */
    public void initComponent() {
    }

    /**
     * Component should dispose system resources or perform another cleanup in this method.
     */
    public void disposeComponent() {
/*
        Frame frame = getFrame();
        if (dropTarget != null) {
            frame.setGlassPane(dropTarget.getPriorGlassPane());
        }
*/
    }

    public void openFileInProject(File pFile, Project pProject) {
        if (pFile.canRead()) {
            try {
                String tUrl = VfsUtil.fixURLforIDEA(pFile.toURI().toURL().toString());
                final VirtualFile tVirtualFile = VirtualFileManager.getInstance().findFileByUrl(tUrl);
                final FileEditorManager tManager = FileEditorManager.getInstance(pProject);
                FileType tFileType = null;
                if (tVirtualFile != null) {
//                    tFileType = FileTypeManager.getInstance().getFileTypeByFile(tVirtualFile);
                    SwingUtilities.invokeLater(new Runnable() {
                         public void run() {
                             tManager.openFile(tVirtualFile, true);
                         }
                    });
                }
            } catch (MalformedURLException e) {
                log.error(e);
            }
        } else {
            showWarning(pFile);
        }
    }

    private void showWarning(File pFile) {
        Messages.showWarningDialog("Dropped file can't be read: " + pFile.getAbsolutePath()
                , "Warning (Native Neighbourhood)");
    }
}
