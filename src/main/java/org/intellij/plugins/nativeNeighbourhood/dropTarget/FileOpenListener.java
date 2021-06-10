/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 25.08.2005
 * Time: 00:15:04
 */
package org.intellij.plugins.nativeNeighbourhood.dropTarget;

import com.intellij.openapi.project.Project;

import java.io.File;

/**
 * @author frb
 */
public interface FileOpenListener {
    void openFileInProject(File pFile, Project pProject);
}
