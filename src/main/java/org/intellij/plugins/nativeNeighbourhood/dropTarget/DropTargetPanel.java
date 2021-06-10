package org.intellij.plugins.nativeNeighbourhood.dropTarget;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;

public class DropTargetPanel extends JPanel implements DropTargetListener {

    public static final int MESSAGES_YES = 0;

    private FileOpenListener listener;
    private Project project;

//    public Component getPriorGlassPane() {
//        return priorGlassPane;
//    }
//
//    private Component priorGlassPane;

    public DropTargetPanel(Component pComponent, Project pProject,
                           FileOpenListener pListener, Component pPriorGlassPane) {
        listener = pListener;
        project = pProject;
//        priorGlassPane = pPriorGlassPane;
        new DropTarget (/*Component*/pComponent, /*DropTargetListener*/this);
//        new DropTarget (/*Component*/this, /*DropTargetListener*/this);
    }

    public void drop(DropTargetDropEvent pEvent) {
        try {
            Transferable tr = pEvent.getTransferable();
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                pEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                Object o = tr.getTransferData(DataFlavor.javaFileListFlavor);
                if (o instanceof java.util.List) {
                    java.util.List fileList = (java.util.List)o;
                    int tAnswer = MESSAGES_YES;
                    if (fileList.size() > 1) {
                        tAnswer = Messages.showYesNoCancelDialog(project,
                                "You have dropped more than 1 file. Do you want to open all "
                                        + fileList.size() + " files?",
                                "Open multiple files? (Native Neighbourhood)", Messages.getQuestionIcon());
                    }
                    if (tAnswer == MESSAGES_YES) {
                        for (Object aFileList : fileList) {
                            final File file = (File) aFileList;
                            listener.openFileInProject(file, project);
                        }
                    }
                    pEvent.getDropTargetContext().dropComplete(true);
                }
            } else {
                pEvent.rejectDrop();
            }
        } catch (IOException io) {
            io.printStackTrace();
            pEvent.rejectDrop();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
            pEvent.rejectDrop();
        }
    }

    public void dragEnter(DropTargetDragEvent pEvent) {
    }

    public void dragOver(DropTargetDragEvent pEvent) {
    }

    public void dropActionChanged(DropTargetDragEvent pEvent) {
    }

    public void dragExit(DropTargetEvent pEvent) {
    }

}
