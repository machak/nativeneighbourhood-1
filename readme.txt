Release Checklist
-----------------

1. Update version number 
   a) in build.properties
   b) in META-INF/plugin.xml
2. Provide Changes in Release Notes
   a) in META-INF/plugin.xml

3. check-in changed artifacts to subversion

4. tag subversion with release_x_y_z
   using base url https://nativeneighbourhood.googlecode.com/svn/tags

8. Build using target all in build.xml

9. Upload build (NativeNeighbourhood-x.y.z.zip)
   a) to Google Code Site (http://code.google.com/p/nativeneighbourhood/downloads/list)
   b) to JetBrains Plugin Repository (http://plugins.intellij.net/plugin/?id=38)
