To enable debug-messages put the following snippet inside log.xml in IDEA's bin directory.

  <category name="org.intellij.plugins.nativeNeighbourhood">
    <priority value="DEBUG"/>
    <appender-ref ref="CONSOLE-DEBUG"/>
    <appender-ref ref="FILE"/>
  </category>

Please put it in between the last appender and the root-element.