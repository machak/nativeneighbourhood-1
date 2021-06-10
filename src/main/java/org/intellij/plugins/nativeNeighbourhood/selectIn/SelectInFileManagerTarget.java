/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 20.07.2005
 * Time: 00:25:21
 */
package org.intellij.plugins.nativeNeighbourhood.selectIn;

import com.intellij.ide.SelectInTarget;
import com.intellij.ide.SelectInContext;
import org.intellij.plugins.nativeNeighbourhood.actions.ShowAction;
import org.intellij.plugins.nativeNeighbourhood.Configurator;
import org.intellij.plugins.nativeNeighbourhood.Util;

import java.io.File;

/**
 * @author frb
 */
@SuppressWarnings("ComponentNotRegistered")
public class SelectInFileManagerTarget extends ShowAction implements SelectInTarget {

    /** Label for Entry in List of "Select in"-Targets */
    public String toString() {
        return Configurator.getInstance().getFileManagerName();
    }

    public boolean canSelect(SelectInContext pContext) {
        boolean tResult = false;
        File tFile = Util.giveFileForVirtualFile(pContext.getVirtualFile());
        if (tFile != null && tFile.canRead()) {
            tResult = true;
        }
        return tResult;
    }

    public void selectIn(SelectInContext pContext, boolean b) {
        Util.showInFileManager(pContext.getVirtualFile());
    }

    public String getToolWindowId() {
        return SelectInFileManagerTarget.class.getName();
    }

    public String getMinorViewId() {
        return null;
    }

    public float getWeight() {
        return (float)50;
    }

}
