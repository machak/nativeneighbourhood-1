/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 19.07.2005
 * Time: 23:54:18
 */
package org.intellij.plugins.nativeNeighbourhood.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;
import org.intellij.plugins.nativeNeighbourhood.Util;

import javax.swing.*;

/**
 * @author frb
 */
public class ShowAction extends AnAction {

    final static Logger log = Logger.getInstance(ShowAction.class.getName());

    /**
     * Creates a new action with its text, description and icon set to <code>null</code>.
     */
    public ShowAction() {
        Icon tIcon = Util.loadIcon("filemanager.gif");
        getTemplatePresentation().setIcon(tIcon);
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param pEvent Carries information on the invocation place
     */
    public void actionPerformed(AnActionEvent pEvent) {
        DataContext tDataContext = pEvent.getDataContext();
        VirtualFile tVirtualFile = (VirtualFile)tDataContext.getData(PlatformDataKeys.VIRTUAL_FILE.getName());
        Util.showInFileManager(tVirtualFile);
    }
}
