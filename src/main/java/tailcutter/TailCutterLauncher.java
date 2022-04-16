package tailcutter;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;

public class TailCutterLauncher {

    @Option(name = "-o", usage = "Output file name")
    private File outputFile;

    @Option(name = "-c", usage = "Numbers of characters to extract", forbids = {"-n"})
    private int charactersNumber;

    @Option(name = "-n", usage = "Numbers of lines to extract", forbids = {"-c"})
    private int linesNumber = 10;

    @Argument(usage = "Input file names")
    private File[] inputFiles;

    public static void main(String[] args) {
        new TailCutterLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
        }

        try {
            TailCutter tailCutter = new TailCutter(charactersNumber, linesNumber);
            tailCutter.cutTail(inputFiles, outputFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
