package org.intellij.plugins.nativeNeighbourhood;

import java.util.Map;
import java.util.HashMap;

/**
 * For Mac OS X Defaults thanks to Denis N. Antonioli
 * For Linux Gnome Defaults thanks to Nikita Belenkiy
 * For Linux KDE Defaults thanks to Clifton C. Craig
 *
 * For default applications for several platforms look at
 * http://cweiske.de/howto/launch/allinone.html
 * 
 * @author frb
 */
public class Configuration {

    public static final String FALLBACK_ICON_PATH = "/toolbar/unknown.png";

    public static final String WINDOWS     = "windows";
    public static final String MACOSX      = "macosx";
    public static final String LINUX_GNOME = "linuxgnome";
    public static final String LINUX_KDE   = "linuxkde";
    public static final String LINUX_XFCE   = "linuxxfce";

    private static Map<String, Configuration> configurations;

	private String iconsDir;
    private String commonDelimiter;
    private String showCommand;
    private String startCommand;
    private String shellCommand;
    private String fileManagerName;
    private String nativeExtensions;

	private static final String linux_nativeExtensions = "pdf,tar,gz,odt";

	static {

        configurations = new HashMap<String, Configuration>();

        /////////////
        // Windows //
        /////////////

        Configuration windows = new Configuration(WINDOWS);
        windows.iconsDir = "windows";
        windows.commonDelimiter = "";
        windows.showCommand = "explorer.exe /select,$FilePath$";
        windows.startCommand = "rundll32.exe url.dll,FileProtocolHandler $FilePath$";
        windows.shellCommand = "cmd /c \"$PluginsHome$\\NativeNeighbourhood\\classes\\org\\intellij\\plugins\\nativeNeighbourhood\\icons\\windows\\cmd.bat\"";
        /*
            where cmd.bat contains:

start cmd /k

        */
        windows.fileManagerName = "Windows Explorer";
        windows.nativeExtensions = "doc,xls,ppt,mdb,vsd,pdf,exe,lnk,hlp,chm";

        //////////////
        // Mac OS X //
        //////////////

        Configuration macosx = new Configuration(MACOSX);
        macosx.iconsDir = "macosx";
        macosx.commonDelimiter = "|";
        macosx.showCommand = "/bin/sh|$PluginsHome$/NativeNeighbourhood/classes/org/intellij/plugins/nativeNeighbourhood/icons/macosx/reveal.sh|$FilePath$";
        macosx.startCommand = "/usr/bin/open|$FilePath$";
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
        macosx.shellCommand = "/bin/sh|$PluginsHome$/NativeNeighbourhood/classes/org/intellij/plugins/nativeNeighbourhood/icons/macosx/terminal.sh|$FileDir$";
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

        macosx.fileManagerName = "MacOSX Finder";
        macosx.nativeExtensions = "pdf,doc,ppt,xls";

        ///////////////
        // Linux/KDE //
        ///////////////

        Configuration kde = new Configuration(LINUX_KDE);
        kde.iconsDir = "linuxkde";
        kde.commonDelimiter = "|";

//        kde.showCommand = "dolphin|$FileDir$";
        kde.showCommand = "konqueror|--select $FilePath$";

//        kde.startCommand = "xdg-open|exec|$FilePath$";
//        kde.startCommand = "kde-open|exec|$FilePath$";
        kde.startCommand = "kfmclient|exec|$FilePath$";
//        kde.startCommand = "konqueror|--silent|$FilePath$";

        kde.shellCommand = "konsole|--workdir|$FileDir$";

//        kde.fileManagerName = "Dolphin";
        kde.fileManagerName = "Konqueror";

        kde.nativeExtensions = linux_nativeExtensions;

        /////////////////
        // Linux/Gnome //
        /////////////////

        Configuration gnome = new Configuration(LINUX_GNOME);
        gnome.iconsDir = "linuxgnome";
        gnome.commonDelimiter = "|";
        gnome.showCommand = "nautilus|$FileDir$";
        gnome.startCommand = "xdg-open|$FilePath$"; // or gvfs-open, no longer gnome-open since Ubuntu 12.04
        gnome.shellCommand = "gnome-terminal|--working-directory=$FileDir$";
//        gnome.emailCommand = "xdg-email|--attach $FilePath$"; // TODO
        gnome.fileManagerName = "Nautilus";
		
        gnome.nativeExtensions = linux_nativeExtensions;

        Configuration xfce = new Configuration(LINUX_XFCE);
        xfce.iconsDir = "linuxxfce";
        xfce.commonDelimiter = "|";
        xfce.showCommand = "thunar|$FileDir$";
        xfce.startCommand = "xdg-open|$FilePath$"; // or gvfs-open, no longer gnome-open since Ubuntu 12.04
        xfce.shellCommand = "xfce4-terminal|--working-directory=$FileDir$";
        xfce.fileManagerName = "Thunar";
        xfce.nativeExtensions = linux_nativeExtensions;

    }

    public Configuration(String pId) {
        configurations.put(pId, this);
    }

    public static Configuration getConfiguration(String id) {
        return configurations.get(id);
    }

    public String getIconsDir() {
        return iconsDir;
    }

    public String getCommonDelimiter() {
        return commonDelimiter;
    }

    public String getShowCommand() {
        return showCommand;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public String getFileManagerName() {
        return fileManagerName;
    }

    public String getNativeExtensions() {
        return nativeExtensions;
    }
}
