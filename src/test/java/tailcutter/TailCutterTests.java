package tailcutter;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class TailCutterTests {
    private final String NEW_LINE = System.lineSeparator();

    private File getFile(String fileName) {
        return new File("src\\test\\resources\\TestFiles\\" + fileName);
    }

    private boolean assertFileContent(File file, String expected) throws IOException {
        File expectedFile = getFile("expected");
        BufferedWriter bw = new BufferedWriter(new FileWriter(expectedFile));
        bw.write(expected);
        bw.close();
        return FileUtils.contentEquals(file, expectedFile);
    }

    @Test
    public void cutFileTailTest() throws IOException {
        File outputFile = getFile("ou");
        // test one file (lines)
        File firstInputFile = getFile("in1");

        TailCutter tc1 = new TailCutter(0, 5);
        tc1.cutTail(new File[]{firstInputFile}, outputFile);
        assertTrue(assertFileContent(outputFile,
                firstInputFile + NEW_LINE +
                        "cd" + NEW_LINE +
                        "ef" + NEW_LINE +
                        "gh" + NEW_LINE +
                        "ij" + NEW_LINE +
                        "kl"));
        FileUtils.write(outputFile, "");

        // test a few files + empty file (characters)
        File secondInputFile = getFile("in2");
        File thirdInputFile = getFile("in3");

        TailCutter tc2 = new TailCutter(15, 0);
        tc2.cutTail(new File[]{secondInputFile, thirdInputFile}, outputFile);
        assertTrue(assertFileContent(outputFile,
                secondInputFile + NEW_LINE +
                        thirdInputFile + NEW_LINE +
                        "end of the file"));
        FileUtils.write(outputFile, "");
    }

    //incorrect file name
    @Test(expected = IOException.class)
    public void incorrectFileNameTest() throws IOException {
        TailCutter tc = new TailCutter(0, 5);
        tc.cutTail(new File[]{getFile("in111")}, getFile("ou"));
    }

    //negative number of lines
    @Test(expected = IllegalArgumentException.class)
    public void incorrectParameterValueTest() throws IllegalArgumentException, IOException {
        TailCutter tc = new TailCutter(0, -5);
        tc.cutTail(new File[]{getFile("in1")}, getFile("ou"));
    }

    @Test
    public void consoleTest() throws FileNotFoundException {
        // test with input file
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(output));

        String[] args1 = {"-n", "5", getFile("in4").getPath()};
        TailCutterLauncher.main(args1);

        assertEquals(
                getFile("in4") + NEW_LINE +
                        "a4" + NEW_LINE +
                        "a5" + NEW_LINE +
                        "a6" + NEW_LINE +
                        "a7" + NEW_LINE +
                        "a8" + NEW_LINE, output.toString());

        // test without input file
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        System.setIn(new FileInputStream(getFile("consoleInput")));
        InputStream oldIn = System.in;

        String[] args2 = {"-c", "9"};
        TailCutterLauncher.main(args2);
        System.setIn(new FileInputStream(getFile("consoleInput")));
        assertEquals("this file" + NEW_LINE, output.toString());

        System.setIn(oldIn);
        System.setOut(oldOut);
    }
}