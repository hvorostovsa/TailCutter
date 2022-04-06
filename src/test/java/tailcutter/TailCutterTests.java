package tailcutter;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class TailCutterTests {

    private boolean assertFileContent(String fileName, String expected) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("files/expected"));
        bw.write(expected);
        bw.close();
        return FileUtils.contentEquals(new File(fileName), new File("files/expected"));
    }

    @Test
    public void cutFileTailTest() throws IOException {
        File file = new File("files/ou");

        TailCutter tc1 = new TailCutter(0, 5);
        tc1.cutFileTail(new String[] {"files/in1"}, "files/ou");
        assertTrue(assertFileContent("files/ou", """
                files/in1
                cd
                ef
                gh
                ij
                kl"""));
        FileUtils.write(file, "");


        TailCutter tc2 = new TailCutter(5, 0);
        tc2.cutFileTail(new String[] {"files/in2", "files/in3"}, "files/ou");
        assertTrue(assertFileContent("files/ou", """
                files/in2              
                files/in3
                wf
                jh"""));
        FileUtils.write(file, "");


    }

    @Test
    public void incorrectFileNameTest() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream old = System.err;
        System.setErr(new PrintStream(output));

        TailCutter tc1 = new TailCutter(0, 5);
        tc1.cutFileTail(new String[] {"files/in111"}, "files/ou");
        assertEquals("files\\in111 (Не удается найти указанный файл)" + System.lineSeparator(), output.toString());

        System.setErr(old);

    }

    @Test
    public void consoleTest() {
        ByteArrayOutputStream outputOut = new ByteArrayOutputStream();
        ByteArrayOutputStream outputErr = new ByteArrayOutputStream();

        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;

        System.setOut(new PrintStream(outputOut));
        System.setErr(new PrintStream(outputErr));

        // everything is fine
        String[] args1 = {"-n", "5", "files/in4"};
        TailCutterLauncher.main(args1);

        assertEquals("""
                files/in4
                a4
                a5
                a6
                a7
                a8""" + System.lineSeparator(), outputOut.toString());

        // if -c and -n are used together
        String[] args2 = {"-n"};
        TailCutterLauncher.main(args2);

        assertEquals("Option \"-n\" takes an operand" + System.lineSeparator(), outputErr.toString());

        System.setOut(oldOut);
        System.setErr(oldErr);

    }
}