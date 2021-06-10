/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 19.07.2005
 * Time: 23:31:18
 */
package org.intellij.plugins.nativeNeighbourhood.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.nativeNeighbourhood.Util;

import javax.swing.*;

/**
 * @author frb
 */
public class StartAction extends AnAction {

    final static Logger log = Logger.getInstance(StartAction.class.getName());

    /**
     * Creates a new action with its text, description and icon set to <code>null</code>.
     */
    public StartAction() {
        Icon tIcon = Util.loadIcon("defaultapplication.gif");
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
	    Util.startWithDefaultApplication(tVirtualFile);
    }

/*
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        if (e.getPlace().equals(ActionPlaces.MAIN_MENU)) {
            presentation.setText("My Menu item name");
        } else if (e.getPlace().equals(ActionPlaces.MAIN_TOOLBAR)) {
            presentation.setText("My Toolbar item name");
        }
    }
*/
}
