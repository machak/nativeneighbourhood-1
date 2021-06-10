package org.intellij.plugins.nativeNeighbourhood.dropTarget;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

@SuppressWarnings({"unchecked", "deprecation"})
public class DropDemo extends JFrame
                      implements ListSelectionListener
{
    private DroppableList list;
    private JTextField fileName;

    public DropDemo()
    {
        super("ListDemo");

        //Create the list and put it in a scroll pane
        list = new DroppableList();
        DefaultListModel listModel = (DefaultListModel)list.getModel();
        list.setCellRenderer(new CustomCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);

        String dirName = new String(".");
        String filelist[] = new File(dirName).list();
        for (int i=0; i < filelist.length ; i++ )
        {
            String thisFileSt = dirName+filelist[i];
            File thisFile = new File(thisFileSt);
            if (thisFile.isDirectory())
                continue;
            try {
                listModel.addElement(makeNode(thisFile.getName(),
                                              thisFile.toURL().toString(),
                                              thisFile.getAbsolutePath()));
            } catch (java.net.MalformedURLException e){
            }
        }

        fileName = new JTextField(50);
        int tIndex = list.getSelectedIndex();
        if (tIndex == -1) tIndex = 0;
        String name = listModel.getElementAt(
                              tIndex).toString();
        fileName.setText(name);

        //Create a panel that uses FlowLayout (the default).
        JPanel buttonPane = new JPanel();
        buttonPane.add(fileName);

        Container contentPane = getContentPane();
        contentPane.add(listScrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.NORTH);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            fileName.setText("");
            if (list.getSelectedIndex() != -1)
            {
                String name = list.getSelectedValue().toString();
                fileName.setText(name);
            }
        }
    }

    private static Hashtable makeNode(String name,
                                      String url, String strPath)
    {
        Hashtable hashtable = new Hashtable();
        hashtable.put("name", name);
        hashtable.put("url", url);
        hashtable.put("path", strPath);
        return hashtable;
    }

    public class DroppableList extends JList
        implements DropTargetListener, DragSourceListener, DragGestureListener
    {
        DropTarget dropTarget = new DropTarget (this, this);
        DragSource dragSource = DragSource.getDefaultDragSource();

        public DroppableList()
        {
          dragSource.createDefaultDragGestureRecognizer(
              this, DnDConstants.ACTION_COPY_OR_MOVE, this);
          setModel(new DefaultListModel());
        }

        public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent){}
        public void dragEnter(DragSourceDragEvent DragSourceDragEvent){}
        public void dragExit(DragSourceEvent DragSourceEvent){}
        public void dragOver(DragSourceDragEvent DragSourceDragEvent){}
        public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent){}

        public void dragEnter (DropTargetDragEvent dropTargetDragEvent)
        {
          dropTargetDragEvent.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
        }

        public void dragExit (DropTargetEvent dropTargetEvent) {}
        public void dragOver (DropTargetDragEvent dropTargetDragEvent) {}
        public void dropActionChanged (DropTargetDragEvent dropTargetDragEvent){}

        public synchronized void drop (DropTargetDropEvent dropTargetDropEvent)
        {
            try
            {
                Transferable tr = dropTargetDropEvent.getTransferable();
                if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor))
                {
                    dropTargetDropEvent.acceptDrop (
                        DnDConstants.ACTION_COPY_OR_MOVE);
                    java.util.List fileList = (java.util.List)
                        tr.getTransferData(DataFlavor.javaFileListFlavor);
	                for (Object aFileList : fileList) {
		                File file = (File) aFileList;
		                Hashtable hashtable = new Hashtable();
		                hashtable.put("name", file.getName());
		                hashtable.put("url", file.toURL().toString());
		                hashtable.put("path", file.getAbsolutePath());
		                ((DefaultListModel) getModel()).addElement(hashtable);
	                }
                    dropTargetDropEvent.getDropTargetContext().dropComplete(true);
              } else {
                System.err.println ("Rejected");
                dropTargetDropEvent.rejectDrop();
              }
            } catch (IOException io) {
                io.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            }
        }

        public void dragGestureRecognized(DragGestureEvent dragGestureEvent)
        {
            if (getSelectedIndex() == -1)
                return;
            Object obj = getSelectedValue();
            if (obj == null) {
                // Nothing selected, nothing to drag
                System.out.println ("Nothing selected - beep");
                getToolkit().beep();
            } else {
                Hashtable table = (Hashtable)obj;
                FileSelection transferable =
                  new FileSelection(new File((String)table.get("path")));
                dragGestureEvent.startDrag(
                  DragSource.DefaultCopyDrop,
                  transferable,
                  this);
            }
        }
    }

    public class CustomCellRenderer implements ListCellRenderer
    {
        DefaultListCellRenderer listCellRenderer =
          new DefaultListCellRenderer();
        public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean selected, boolean hasFocus)
        {
            listCellRenderer.getListCellRendererComponent(
              list, value, index, selected, hasFocus);
            listCellRenderer.setText(getValueString(value));
            return listCellRenderer;
        }
        private String getValueString(Object value)
        {
            String returnString = "null";
            if (value != null) {
              if (value instanceof Hashtable) {
                Hashtable h = (Hashtable)value;
                String name = (String)h.get("name");
                String url = (String)h.get("url");
                returnString = name + " ==> " + url;
              } else {
                returnString = "X: " + value.toString();
              }
            }
            return returnString;
        }
    }

    public class FileSelection extends Vector implements Transferable
    {
        final static int FILE = 0;
        final static int STRING = 1;
        final static int PLAIN = 2;
        DataFlavor flavors[] = {DataFlavor.javaFileListFlavor,
                                DataFlavor.stringFlavor,
                                DataFlavor.plainTextFlavor};
        public FileSelection(File file)
        {
            addElement(file);
        }
        /* Returns the array of flavors in which it can provide the data. */
        public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
        }
        /* Returns whether the requested flavor is supported by this object. */
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            boolean b  = false;
            b |=flavor.equals(flavors[FILE]);
            b |= flavor.equals(flavors[STRING]);
            b |= flavor.equals(flavors[PLAIN]);
            return (b);
        }
        /**
         * If the data was requested in the "java.lang.String" flavor,
         * return the String representing the selection.
         */
        public synchronized Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
        if (flavor.equals(flavors[FILE])) {
            return this;
        } else if (flavor.equals(flavors[PLAIN])) {
            return new StringReader(((File)elementAt(0)).getAbsolutePath());
        } else if (flavor.equals(flavors[STRING])) {
            return((File)elementAt(0)).getAbsolutePath();
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
        }
    }

    public static void main(String s[])
    {
        JFrame frame = new JFrame();
        Component tComponent = frame.getGlassPane();
        if (tComponent!=null) {
            System.out.println(
                    "\ttComponent.getClass().getName() = " + tComponent.getClass().getName());
        }
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}