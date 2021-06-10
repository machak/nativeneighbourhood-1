import junit.framework.TestCase;

import java.io.File;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: frank
 * Date: 25.12.2007
 * Time: 08:33:02
 */
public class RuntimeTest extends TestCase {

    public void testGnomeStart() {
        String tFilePath = "/home/frank/IdeaProjects/RubyTest/with space/jruby-bin-1.0.3.tar.gz";
        File tDir = new File(".");
        String[] tCommandArray = new String[] {
//                "/bin/bash",
//                "-c",
                "gnome-open",
                tFilePath
        };
        try {
            System.out.println("tCommandArray = " + Arrays.asList(tCommandArray));
            if (tDir == null) {
//                log.debug("executing command: " + tCommandArray);
                Runtime.getRuntime().exec(tCommandArray);
            } else {
//                log.debug("executing command: '" + tCommandArray + "' in dir " + pDir);
                Runtime.getRuntime().exec(tCommandArray, null, tDir);
            }
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
