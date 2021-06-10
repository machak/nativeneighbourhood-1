package org.intellij.plugins.nativeNeighbourhood.dropTarget;/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 24.08.2005
 * Time: 23:47:44
 */

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;

/**
 * @author frb
 */
@SuppressWarnings("deprecation")
public class Test {

    /**
     * test
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Component tComponent = frame.getGlassPane();
        String tGlassName = tComponent.getClass().getName();
        System.out.println("\ttGlassName = " + tGlassName);
        frame.setGlassPane(new DropTargetPanel(frame, null, new FileOpenListener() {
            public void openFileInProject(File pFile, Project pProject) {
                try {
                    System.out.println("\tpFile.getAbsolutePath() = " + pFile.getAbsolutePath());
                    System.out.println(VfsUtil.fixURLforIDEA(pFile.toURL().toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }, tComponent));
        tGlassName = tComponent.getClass().getName();
        System.out.println("\ttGlassName = " + tGlassName);
        frame.pack();
        frame.setVisible(true);
        //

    }
}
