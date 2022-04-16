package tailcutter;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class TailCutterTests {
    private final String NEW_LINE = System.lineSeparator();

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
        tc1.cutTail(new File[]{new File("files/in1")}, new File("files/ou"));
        assertTrue(assertFileContent("files/ou",
                "files\\in1" + NEW_LINE +
                        "cd" + NEW_LINE +
                        "ef" + NEW_LINE +
                        "gh" + NEW_LINE +
                        "ij" + NEW_LINE +
                        "kl"));
        FileUtils.write(file, "");


        TailCutter tc2 = new TailCutter(5, 0);
        tc2.cutTail(new File[]{new File("files/in2"), new File("files/in3")}, new File("files/ou"));
        assertTrue(assertFileContent("files/ou",
                "files\\in2" + NEW_LINE +
                        "files\\in3" + NEW_LINE +
                        "fsefs"));
        FileUtils.write(file, "");
    }

    //incorrect file name
    @Test(expected = IOException.class)
    public void incorrectFileNameTest() throws IOException {
        TailCutter tc = new TailCutter(0, 5);
        tc.cutTail(new File[]{new File("files/in111")}, new File("files/ou"));
    }

    //negative number of lines
    @Test(expected = IllegalArgumentException.class)
    public void incorrectParameterValueTest() throws IllegalArgumentException, IOException {
        TailCutter tc = new TailCutter(0, -5);
        tc.cutTail(new File[]{new File("files/in1")}, new File("files/ou"));
    }

    @Test
    public void consoleTest() {
        ByteArrayOutputStream outputOut = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outputOut));

        // everything is fine
        String[] args1 = {"-n", "5", "files/in4"};
        TailCutterLauncher.main(args1);

        assertEquals(
                "files\\in4" + NEW_LINE +
                        "a4" + NEW_LINE +
                        "a5" + NEW_LINE +
                        "a6" + NEW_LINE +
                        "a7" + NEW_LINE +
                        "a8" + NEW_LINE, outputOut.toString());


        System.setOut(oldOut);
    }
}