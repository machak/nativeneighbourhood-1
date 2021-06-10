package org.intellij.plugins.nativeNeighbourhood;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: frank
 * Date: 10.11.2009
 */
public class NativeFileTypeFactory extends FileTypeFactory {

      public void createFileTypes(final @NotNull FileTypeConsumer consumer) {
        consumer.consume(new NativeFileType());
      }
}
