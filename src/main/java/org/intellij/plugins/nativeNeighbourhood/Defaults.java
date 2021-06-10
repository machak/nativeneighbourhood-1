/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 04.08.2005
 * Time: 09:34:49
 */
package org.intellij.plugins.nativeNeighbourhood;

/**
 * @author frb
 * For Mac OS X Defaults thanks to Denis N. Antonioli
 */
public interface Defaults {

    /////////////
    // Windows //
    /////////////

    public static final String WINDOWS_ICONS_DIR = "windows";

    public static final String WINDOWS_COMMON_DELIMS = "";

    public static final String WINDOWS_SHOW_COMMAND =
            "explorer.exe /select,$FilePath$";

    public static final String WINDOWS_START_COMMAND =
            "rundll32.exe url.dll,FileProtocolHandler $FilePath$";

    public static final String WINDOWS_SHELL_COMMAND =
            "cmd /c \"$PluginsHome$\\NativeNeighbourhood\\classes\\org\\intellij\\plugins\\nativeNeighbourhood\\icons\\windows\\cmd.bat\"";
    /*
        where cmd.bat contains:

        start cmd /k
    */

    public static final String WINDOWS_FILE_MANAGER_NAME =
            "Windows Explorer";

    public static final String WINDOWS_NATIVE_EXTENSIONS =
            "doc,xls,ppt,mdb,vsd,pdf,exe,lnk,hlp,chm";


    //////////////
    // Mac OS X //
    //////////////


    public static final String MACOSX_ICONS_DIR = "macosx";

    public static final String MACOSX_COMMON_DELIMS = "|";

    public static final String MACOSX_START_COMMAND =
            "/usr/bin/open|$FilePath$";

    public static final String MACOSX_SHOW_COMMAND =
            "/bin/sh|$PluginsHome$/NativeNeighbourhood/classes/org/intellij/plugins/nativeNeighbourhood/icons/macosx/reveal.sh|$FilePath$";

    /*
        where reveal.sh contains:

#!/bin/sh
#
# Reveal a file in the Finder.
#
# The script takes one argument, the qualified name of a file.
#
# Note that the script is necessary because osascript before osx 10.4 could not
# pass arguments.
#

/usr/bin/osascript <<EOF
	tell application "Finder"
		reveal (POSIX file "$1") as alias
		activate
	end tell
EOF

    */

    public static final String MACOSX_SHELL_COMMAND =
            "/bin/sh|$PluginsHome$/NativeNeighbourhood/classes/org/intellij/plugins/nativeNeighbourhood/icons/macosx/terminal.sh|$FileDir$";
    /*
        where terminal.sh contains:

#!/bin/sh
#
# Reveal a directory in the Terminal.
#
# The script takes one argument, the qualified name of a directory.
#
# Note that the script is necessary because osascript before osx 10.4 could not
# pass arguments.
#

/usr/bin/osascript <<EOF
	tell application "Terminal"
		do script with command "cd " & quote & "$1" & quote
		activate
	end tell
EOF

    */

    public static final String MACOSX_FILE_MANAGER_NAME = "MacOSX Finder";

    public static final String MACOSX_NATIVE_EXTENSIONS = "pdf,doc,ppt,xls";

//    public static final String FALLBACK_ICON_PATH = "/org/intellij/plugins/nativeNeighbourhood/icons/windows/";
    public static final String FALLBACK_ICON_PATH = "/toolbar/unknown.png";
}
