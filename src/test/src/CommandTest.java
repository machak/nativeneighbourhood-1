/*
 * Created by IntelliJ IDEA.
 * User: frb
 * Date: 04.08.2005
 * Time: 12:05:32
 */

import java.io.*;

/**
 * @author frb
 */
public class CommandTest {

    /**
     * test
     */
    public static void main(String[] args) throws IOException {
        String tCommand = "cmd";
        File tDir = new File("");
//        Process tProcess = Runtime.getRuntime().exec(tCommand, null, tDir);
//        Process tProcess = Runtime.getRuntime().exec(new String[] {"cmd", "/k", tDir.getPath()}, null, tDir);
//        Process tProcess = Runtime.getRuntime().exec(new String[] {"cmd", "/k", tDir.getPath()});
//        Process tProcess = Runtime.getRuntime().exec("start cmd /k \"" + tDir.getAbsolutePath() + "\"");
//        Process tProcess = Runtime.getRuntime().exec("start cmd");
        Process tProcess = Runtime.getRuntime().exec("cmd /c \"C:\\Dokumente und Einstellungen\\frb\\IdeaProjects\\NativeNeighbourhood\\src\\org\\intellij\\plugins\\nativeNeighbourhood\\icons\\windows\\cmd.bat\"");
//        Process tProcess = Runtime.getRuntime().exec("cmd /k");
//        Process tProcess = Runtime.getRuntime().exec("cmd /k", null, tDir); //error 267
//        int tExitValue = tProcess.exitValue();
//        System.out.println("\ttExitValue = " + tExitValue);
/*
        try {
            String line;
            Process p = Runtime.getRuntime().exec("\"cmd /k\"");
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        }
        catch (Exception err) {
            err.printStackTrace();
        }
*/
/*
        try
         {
             Runtime rt = Runtime.getRuntime();
             Process proc = rt.exec(new String[] { "cmd", "/k", "" });
             InputStream stderr = proc.getErrorStream();
             InputStream stdout = proc.getInputStream();
             InputStreamReader isr = new InputStreamReader(stdout);
             BufferedReader br = new BufferedReader(isr);
             String line = null;
             System.out.println("<ERROR>");
             while ( (line = br.readLine()) != null)
                 System.out.println(line);
             System.out.println("</ERROR>");
             int exitVal = proc.waitFor();
             System.out.println("Process exitValue: " + exitVal);
         } catch (Throwable t)
           {
             t.printStackTrace();
           }

*/
    }
}
