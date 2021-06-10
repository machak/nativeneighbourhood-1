/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 04.08.2005
 * Time: 09:33:03
 */
package org.intellij.plugins.nativeNeighbourhood;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PathUtil;

import javax.swing.*;
import java.io.File;
import java.util.StringTokenizer;
import java.net.URL;

/**
 * @author frb
 */
public abstract class Util {

    public static final Logger log = Logger.getInstance(Configurator.class.getName());

    public static void executeCommandOnVirtualFile(String pCommand, String pDelims, VirtualFile pFile, File pDir) {
        log.debug("\tpFile.getUrl() = " + pFile.getUrl());
        File tFile = giveFileForVirtualFile(pFile);
        if (tFile != null) {
            executeCommand(pCommand, pDelims, tFile, pDir);
        }
    }

    /** calls System.exec(String[]), requires pFile != null */
    public static void executeCommand(String pCommand, String pDelims, File pFile, File pDir) {
        log.debug("Util.executeCommand with delims \"" + pDelims + "\"");
        if (pDelims==null || pDelims.length() == 0) {
            executeCommand(pCommand, pFile, pDir);
        } else {
            String[] tCommandArray = tokenize(pCommand, pDelims);
            replaceAllPlaceholders(tCommandArray, pFile);
            if (Configurator.debug()) {
                Messages.showInfoMessage(format(tCommandArray), "Executing command (Native Neighbourhood)");
                log.info(format(tCommandArray)); // TODO: remove...
            }
            log.info("Native Neighbourhood executing command:\n\t" + format(tCommandArray));
            try {
                if (pDir == null) {
    //                log.debug("executing command: " + tCommandArray);
                    Runtime.getRuntime().exec(tCommandArray);
                } else {
    //                log.debug("executing command: '" + tCommandArray + "' in dir " + pDir);
                    Runtime.getRuntime().exec(tCommandArray, null, pDir);
                }
            } catch (Exception ex) {
                log.error(ex);
                Messages.showErrorDialog(ex.getLocalizedMessage(),
                        "Error executing command '" + format(tCommandArray) + "'");
            }
        }
    }

    /** calls System.exec(String), requires pFile != null */
    public static void executeCommand(String pCommand, File pFile, File pDir) {
        String tCommand = replaceAllPlaceholders(pCommand, pFile);
        if (Configurator.debug()) {
            Messages.showInfoMessage(tCommand, "Executing command (Native Neighbourhood)");
        }
        log.info("Native Neighbourhood executing command:\n\t" + tCommand);
        try {
            if (pDir == null) {
//                log.debug("executing command: " + tCommand);
                Runtime.getRuntime().exec(tCommand);
            } else {
//                log.debug("executing command: '" + tCommand + "' in dir " + pDir);
                Runtime.getRuntime().exec(tCommand, null, pDir);
            }
        } catch (Exception ex) {
            log.error(ex);
            Messages.showErrorDialog(ex.getLocalizedMessage(),
                    "Error executing command '" + tCommand + "'");
        }
    }

    private static String[] tokenize(String pCommand, String pDelims) {
        log.debug("Util.tokenize with delims \"" + pDelims + "\"");
        StringTokenizer tTokenizer = new StringTokenizer(pCommand, pDelims);
        String[] tResult = new String[tTokenizer.countTokens()];
        int i = 0;
        while (tTokenizer.hasMoreTokens()) {
            String tToken = tTokenizer.nextToken();
            log.debug("\ttToken = " + tToken);
            tResult[i++] = tToken;
        }
        return tResult;
    }

    private static String format(String[] pCommand) {
        StringBuilder tResult = new StringBuilder();
        for (String aPCommand : pCommand) {
            tResult.append(aPCommand);
            tResult.append("\n");
        }
        return tResult.toString();
    }

    private static void replaceAllPlaceholders(String[] pCommand, File pFile) {
        for (int i = 0; i < pCommand.length; i++) {
            pCommand[i] = replaceAllPlaceholders(pCommand[i], pFile);
        }
    }

    private static String replaceAllPlaceholders(String pCommand, File pFile) {
        log.debug("Util.replaceAllPlaceholders");
        String tCommand = pCommand;
        String tFilePath = computePath(pFile);
        String tFileDir;
        if (pFile.isDirectory()) {
            tFileDir = computePath(pFile);
        } else {
            tFileDir = computePath(pFile.getParentFile());
        }
        String tFileName = pFile.getName();
        String tPluginHome = computePath(new File(
                com.intellij.openapi.application.PathManager.getPluginsPath()));
        if (Configurator.debug()) {
            log.debug("\ttFilePath = " + tFilePath);
            log.debug("\ttFileDir = " + tFileDir);
            log.debug("\ttFileName = " + tFileName);
            log.debug("\ttPluginHome = " + tPluginHome);
        }
        tCommand = replacePlaceholder("$FilePath$", tCommand, tFilePath);
        tCommand = replacePlaceholder("$FileDir$", tCommand, tFileDir);
        tCommand = replacePlaceholder("$FileName$", tCommand, tFileName);
        tCommand = replacePlaceholder("$PluginsHome$", tCommand, tPluginHome);
        return tCommand;
    }

    private static String computePath(File pFile) {
        String tFilePath = pFile.getAbsolutePath();
//        File tFile = pFile;
        if (tFilePath.contains("~")) {
            tFilePath = replacePlaceholder("~", tFilePath, System.getProperty("user.home"));
//            tFile = new File(tFilePath);
        }
/*
        try {
            tFilePath = tFile.getCanonicalPath();
        } catch (IOException e) {
            tFilePath = tFile.getAbsolutePath();
        }
*/
        return tFilePath;
    }

    private static String replacePlaceholder(String pPlaceHolder,
                                             String pCommand,
                                             String pValue) {
        String tCommand;
        int tIndex = pCommand.indexOf(pPlaceHolder);
        if (tIndex > -1) {
            tCommand = pCommand.substring(0, tIndex) + pValue
                    + pCommand.substring(tIndex + pPlaceHolder.length());
        } else {
            tCommand = pCommand;
        }
        return tCommand;
    }

    private static void noFileWarning() {
        Messages.showWarningDialog("No active physical file could be determined.", "Warning (Native Neighbourhood)");
    }

    public static File giveFileForVirtualFile(VirtualFile pVirtualFile) {
        File tResult = null;
        if (pVirtualFile == null) {
            noFileWarning();
        } else {
/*
            String tPath = pVirtualFile.getPath();
            if (tPath.endsWith("!/")) {
                tPath = tPath.substring(0, tPath.length() - 2);
            }
            tResult = new File(tPath);
*/
            String tPath = PathUtil.getCanonicalPath(PathUtil.getLocalPath(pVirtualFile));
            if (tPath != null) {
                tResult = new File(tPath);
            }
        }
        return tResult;
    }

    public static void startWithDefaultApplication(VirtualFile pVirtualFile) {
        File tFile = giveFileForVirtualFile(pVirtualFile);
        if (tFile != null) {
            if (!tFile.isDirectory()) {
//                File tDir = tFile.getParentFile();
                File tDir = null;
                executeCommand(Configurator.getInstance().getStartCommand(),
                        Configurator.getInstance().getStartCommandTokenizerDelims(),
                        tFile, tDir);
            } else {
                Messages.showErrorDialog("Cannot execute directory.", "Warning (Native Neighbourhood)");
            }
        }
    }

    public static void showInFileManager(VirtualFile pFile) {
        File tFile = giveFileForVirtualFile(pFile);
        if (tFile != null) {
            executeCommand(Configurator.getInstance().getShowCommand(),
                    Configurator.getInstance().getShowCommandTokenizerDelims(),
                    tFile, null);
        }
    }

    public static void openCommandShell(VirtualFile pFile) {
        File tFile = giveFileForVirtualFile(pFile);
        if (tFile != null) {
            File tDir = tFile;
            if (! tDir.isDirectory()) {
                tDir = tDir.getParentFile();
            }
            executeCommand(Configurator.getInstance().getShellCommand(),
                    Configurator.getInstance().getShellCommandTokenizerDelims(),
                    tFile, tDir);
        }
    }

    public static Icon loadIcon(String pImageFile) {
        String tIconsDir = Configurator.getInstance().getIconsDir();
        return loadIconFromDir(pImageFile, tIconsDir);
    }

    public static Icon loadIconFromDir(String pImageFile, String pIconsDir) {
        String tResourcePath = "/org/intellij/plugins/nativeNeighbourhood/icons/"
                + pIconsDir + "/" + pImageFile;
        Icon tResult = null;
        try {
            URL tUrl = Util.class.getResource(tResourcePath);
            if (tUrl != null) {
                tResult = IconLoader.getIcon(tResourcePath);
            }
        } catch (Exception e) {
            // nothing here, message printed in finally
        } finally {
            if (tResult == null) {
                String msg = "Failed to load icon resource named " + tResourcePath;
                System.err.println(msg);
                log.warn(msg);
                tResult = IconLoader.getIcon(Configuration.FALLBACK_ICON_PATH);
            }
        }
        return tResult;
    }
}
