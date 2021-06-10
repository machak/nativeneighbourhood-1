/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 09.08.2005
 * Time: 23:25:52
 */
package org.intellij.plugins.nativeNeighbourhood.dummyEditor;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;

/**
 * @author frb
 */
public class NativeFileEditorState implements FileEditorState {
    public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
        return true;
    }
}
