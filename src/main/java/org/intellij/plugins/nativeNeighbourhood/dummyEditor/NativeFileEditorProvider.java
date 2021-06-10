/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 09.08.2005
 * Time: 23:11:16
 */
package org.intellij.plugins.nativeNeighbourhood.dummyEditor;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jdom.Element;
import org.intellij.plugins.nativeNeighbourhood.Util;
import org.intellij.plugins.nativeNeighbourhood.Configurator;
import org.jetbrains.annotations.NotNull;

/**
 * @author frb
 * see http://youtrack.jetbrains.com/issue/IDEABKL-4151?projectKey=IDEABKL
 */
public class NativeFileEditorProvider implements ApplicationComponent, FileEditorProvider {

    public static final String EDITOR_TYPE_ID = "NativeNeighbourhood";

    /**
     * Unique name of this component. If there is another component with the same name or
     * name is null internal assertion will occur.
     *
     * @return the name of this component
     */
    @NotNull
    public String getComponentName() {
        return NativeFileEditorProvider.class.getName();
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
    }

    /**
     * @param file file to be tested for acceptance. This
     *             parameter is never <code>null</code>.
     * @return whether the provider can create valid editor for the specified
     *         <code>file</code> or not
     */
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return FileTypeManager.getInstance().getFileTypeByFile(file).getName().equals(
                Configurator.NATIVE_FILE_TYPE_NAME);
    }

    /**
     * Creates editor for the specified pFile. This method
     * is called only if the provider has accepted this pFile (i.e. method {@link #accept(com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)} returned
     * <code>true</code>).
     * The provider should return only valid editor.
     *
     * @return created editor for specified pFile. This method should never return <code>null</code>.
     */
    @NotNull
    public FileEditor createEditor(@NotNull Project pProject, @NotNull VirtualFile pFile) {
        Util.executeCommandOnVirtualFile(Configurator.getInstance().getStartCommand(),
                Configurator.getInstance().getStartCommandTokenizerDelims(), pFile, null);
        FileEditorManager tManager = FileEditorManager.getInstance(pProject);
        Editor tActiveEditor = tManager.getSelectedTextEditor();
        VirtualFile tVirtualFile = null;
        if (tActiveEditor != null) {
            PsiFile tPsiFile = PsiDocumentManager.getInstance(pProject).getPsiFile(tActiveEditor.getDocument());
            if (tPsiFile != null) {
                tVirtualFile = tPsiFile.getVirtualFile();
            }
        }
        return new NativeFileEditor(pProject, pFile, tVirtualFile);

    }

    /**
     * Disposes the specified <code>editor</code>. It is guaranteed that this method is invoked only for editors
     * created with this provider.
     *
     * @param editor editor to be disposed. This parameter is always not <code>null</code>.
     */
    public void disposeEditor(@NotNull FileEditor editor) {
    }

    /**
     * Deserializes state from the specified <code>sourceElemet</code>
     */
    @NotNull
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project, @NotNull VirtualFile file) {
        // close editor
        return new NativeFileEditorState();
    }

    /**
     * Serializes state into the specified <code>targetElement</code>
     */
    public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {
/*
        ActionManager.getInstance().getAction("CloseEditor").actionPerformed(new AnActionEvent(
            new KeyEvent(editor.getComponent(),         // source
                            KeyEvent.KEY_PRESSED,       // id
                            System.currentTimeMillis(), // when
                            KeyEvent.CTRL_MASK,         // modifiers
                            KeyEvent.VK_UNDEFINED,      // key code
                            KeyEvent.CHAR_UNDEFINED),   // key char
                    DataManager.getInstance().getDataContext(),
                    ActionPlaces.MAIN_MENU,
                    null, // Presentation is not public but may not be null, either
                    ActionManager.getInstance(),
                    0 // modifiers
        ));
*/

    }

    /**
     * @return id of type of the editors that are created with this FileEditorProvider. Each FileEditorProvider should have
     *         unique non null id. The id is used for saving/loading of EditorStates.
     */
    @NotNull
    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    /**
     * @return policy that specifies how show editor created via this provider be opened
     * @see com.intellij.openapi.fileEditor.FileEditorPolicy#NONE
     * @see com.intellij.openapi.fileEditor.FileEditorPolicy#HIDE_DEFAULT_EDITOR
     * @see com.intellij.openapi.fileEditor.FileEditorPolicy#PLACE_BEFORE_DEFAULT_EDITOR
     */
    @NotNull
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.NONE;
    }
}
