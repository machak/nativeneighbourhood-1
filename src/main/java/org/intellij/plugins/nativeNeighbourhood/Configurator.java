/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 04.08.2005
 * Time: 09:56:09
 */
package org.intellij.plugins.nativeNeighbourhood;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;
import org.intellij.plugins.nativeNeighbourhood.util.AbstractConfigurator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.List;

/**
 * Configuration and Management of "Native Neighbourhood"-Filetype
 *
 * @author frb
 */
public class Configurator extends AbstractConfigurator implements ApplicationComponent {

	final static Logger log = Logger.getInstance(Configurator.class.getName());

    public static final String NATIVE_FILE_TYPE_NAME = "Native Neighbourhood";
    private static final String NATIVE_FILE_TYPE_DESCRIPTION = "Native Neighbourhood files";

    private NativeFileType nativeFileType;

    /** default configuration used to fill incomplete configurations read from other.xml */
    private static Configuration defaultConfig = Configuration.getConfiguration(Configuration.WINDOWS);

    /** Subdirectory of /org/intellij/plugins/nativeNeighbourhood/icons/ containing the platform specific icons */
    private String iconsDir = null;

    private String showCommand = null;
    private String showCommandTokenizerDelims = null;
    private String startCommand = null;
    private String startCommandTokenizerDelims = null;
    private String shellCommand = null;
    private String shellCommandTokenizerDelims = null;
    private String filemanagerName = null;
    private String nativeExtensions = null;

    public static boolean debug() {
        return false;
    }

    public static Configurator getInstance() {
        Application application = ApplicationManager.getApplication();
        return application.getComponent(Configurator.class);
    }

    /**
     * name for the component section in other.xml
     */
    @NotNull
    public String getComponentName() {
        return "NativeNeighbourhoodPlugin";
    }

    private void registerFileType() {
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        // already registered?
        FileType[] filetypeArray = fileTypeManager.getRegisteredFileTypes();
        boolean alreadyRegistered = false;
        for (FileType tType : filetypeArray) {
            //            if (tType.getName().equals(NATIVE_FILE_TYPE_NAME)) {
            if (tType instanceof NativeFileType) {
                alreadyRegistered = true;
                nativeFileType = (NativeFileType) tType;
                break;
            }
        }
        if (!alreadyRegistered) {
            nativeFileType = new NativeFileType();
            nativeFileType.setName(NATIVE_FILE_TYPE_NAME);
            nativeFileType.setDescription(NATIVE_FILE_TYPE_DESCRIPTION);
            String tExtensions = getNativeExtensions();
            List<String> tList = new ArrayList<String>();
            StringTokenizer tTokenizer = new StringTokenizer(tExtensions, ",;:/ ");
            while (tTokenizer.hasMoreTokens()) {
                String tExtension = tTokenizer.nextToken().trim();
                tList.add(tExtension);
            }
            String[] tArray = tList.toArray(new String[tList.size()]);
	        fileTypeManager.registerFileType(nativeFileType, tArray);
        }
        nativeFileType.setIcon(Util.loadIconFromDir("nativefiletype.gif", getIconsDir()));
    }

    /**
     * called from writeExternal if no configuration is found in other.xml
     * (writeExternal is called prior to readExternal)
     */
    protected void initializeDefaultConfig() {
        log.warn("no config found in options/other.xml");
        String platform;
        if (isMacOSXPlatform()) {
            platform = Configuration.MACOSX;
        } else if (isLinuxGnomePlatform()) {
            platform = Configuration.LINUX_GNOME;
        } else if (isLinuxKdePlatform()) {
            platform = Configuration.LINUX_KDE;
        } else if (isLinuxXfcePlatform()) {
            platform = Configuration.LINUX_XFCE;
        } else { // default: Windows
            platform = Configuration.WINDOWS;
        }
        log.warn("--> initializing with default configuration for detected platform " + platform);
        useConfiguration(Configuration.getConfiguration(platform));
        // register filetype using read extensions
        registerFileType();
    }

    private void useConfiguration(Configuration config) {
        iconsDir = config.getIconsDir();
        // commands
        startCommand = config.getStartCommand();
        showCommand = config.getShowCommand();
        shellCommand = config.getShellCommand();
        // tokenizer-delimiters
        startCommandTokenizerDelims = config.getCommonDelimiter();
        showCommandTokenizerDelims = config.getCommonDelimiter();
        shellCommandTokenizerDelims = config.getCommonDelimiter();
        // labels
        filemanagerName = config.getFileManagerName();
        // extensions
        nativeExtensions = config.getNativeExtensions();
    }

    /**
     * recommended by Apple, http://developer.apple.com/technotes/tn2002/tn2110.html
     * @return if the platform is Mac OS X
     */
    private boolean isMacOSXPlatform() {
        String tOsName = System.getProperty("os.name").toLowerCase();
        return tOsName.startsWith("mac os x");
    }

    /**
     * @return if the platform is Linux/Gnome
     */
    private boolean isLinuxGnomePlatform() {

        String tDesktop = System.getProperty("sun.desktop");
        return tDesktop != null && tDesktop.toLowerCase().contains("gnome");
/*

    The following alternative only works if the outer environment has been parsed using

Process p = Runtime.getRuntime().exec("env");
InputStream is = p.getInputStream();
// parse name-value-pairs and add to system properties

    (for windows it is somewhat more complicated, look...)
    http://www.jguru.com/faq/view.jsp?EID=11422

*/
/*
        String tGnomeSessionId = System.getProperty("GNOME_DESKTOP_SESSION_ID");
        return tGnomeSessionId != null;
*/
    }

    /**
     * @return if the platform is Linux/Kde
     */
    private boolean isLinuxKdePlatform() {
        String tDesktop = System.getProperty("sun.desktop");
        return tDesktop != null && tDesktop.toLowerCase().contains("kde");
/*

see isLinuxGnomePlatform

        String tKdeFullSession = System.getProperty("KDE_FULL_SESSION");
        return tKdeFullSession != null && "true".equals(tKdeFullSession.toLowerCase());
*/
    }

    private boolean isLinuxXfcePlatform() {
        String tDesktop = System.getenv("XDG_CURRENT_DESKTOP");
        return tDesktop != null && "xfce".equalsIgnoreCase(tDesktop);
    }

    /** updateNativeExtensions */
    protected void updateConfigPriorWrite() {
        // updateNativeExtensions
        FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        String[] tArray = fileTypeManager.getAssociatedExtensions(nativeFileType);
        StringBuilder tExtensions = new StringBuilder(140);
        for (int i = 0; i < tArray.length; i++) {
            String tExt = tArray[i];
            if (i > 0) {
                tExtensions.append(",");
            }
            tExtensions.append(tExt);
        }
        nativeExtensions = tExtensions.toString();
    }

    public String getShowCommandTokenizerDelims() {
        return showCommandTokenizerDelims;
    }

    public String getStartCommandTokenizerDelims() {
        return startCommandTokenizerDelims;
    }

    public String getShellCommandTokenizerDelims() {
        return shellCommandTokenizerDelims;
    }

    protected void readConfig(Element pElement) {
	    log.debug("readConfig");
        iconsDir = readOptionValue(pElement, "iconsDir",
                defaultConfig.getIconsDir());
        // commands
        startCommand = readOptionValue(pElement, "executeWithDefaultAppCommand",
                defaultConfig.getStartCommand());
        showCommand = readOptionValue(pElement, "showInFileManagerCommand",
                defaultConfig.getShowCommand());
        shellCommand = readOptionValue(pElement, "openCommandShellInCommand",
                defaultConfig.getShellCommand());
        startCommandTokenizerDelims = readDelimsValue(pElement, "executeWithDefaultAppCommand",
                defaultConfig.getCommonDelimiter());
        showCommandTokenizerDelims = readDelimsValue(pElement, "showInFileManagerCommand",
                defaultConfig.getCommonDelimiter());
        shellCommandTokenizerDelims = readDelimsValue(pElement, "openCommandShellInCommand",
                defaultConfig.getCommonDelimiter());
        // labels
        filemanagerName = readOptionValue(pElement, "selectInFileManagerName",
                defaultConfig.getFileManagerName());
        // extensions
        nativeExtensions = readOptionValue(pElement, "nativeExtensions",
                defaultConfig.getNativeExtensions());
        // register filetype using read extensions
        registerFileType();
    }

    protected String readDelimsValue(Element element, String pName, String pDefault) {
        String tResult = pDefault;
        Element tOption = element.getChild(pName);
        if (tOption != null) {
            tResult = tOption.getAttributeValue("tokenizerDelims");
        }
        return tResult;
    }

    protected void writeConfig(Element pElement) {
	    log.debug("writeConfig");
        pElement.addContent(createOptionElement("iconsDir", iconsDir));
        // commands
        Element tElement = createOptionElement("executeWithDefaultAppCommand", startCommand);
        tElement.setAttribute("tokenizerDelims", startCommandTokenizerDelims);
        pElement.addContent(tElement);

        tElement = createOptionElement("showInFileManagerCommand", showCommand);
        tElement.setAttribute("tokenizerDelims", showCommandTokenizerDelims);
        pElement.addContent(tElement);

        tElement = createOptionElement("openCommandShellInCommand", shellCommand);
        tElement.setAttribute("tokenizerDelims", shellCommandTokenizerDelims);
        pElement.addContent(tElement);
        // labels
        pElement.addContent(createOptionElement("selectInFileManagerName", filemanagerName));
        // extensions
        pElement.addContent(createOptionElement("nativeExtensions", nativeExtensions));
    }

    public String getIconsDir() {
        return iconsDir;
    }

    /**
     * used in SelectInTarget-Popup
     */
    public String getFileManagerName() {
        return filemanagerName;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getShowCommand() {
        return showCommand;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public String getNativeExtensions() {
        return nativeExtensions;
    }

}