package tailcutter;

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class TailCutter {

    private final int charactersNumber;
    private final int linesNumber;
    private final String NEW_LINE = System.lineSeparator();

    public TailCutter(int charactersNumber, int linesNumber) {
        if (charactersNumber >= 0 && linesNumber >= 0) {
            this.charactersNumber = charactersNumber;
            this.linesNumber = linesNumber;
        } else throw new IllegalArgumentException("Number of characters or lines cannot be less than 0");
    }

    public void cutTail(File[] inputFiles, File outputFile) throws IOException {
        String text = findAllTales(inputFiles);
        if (outputFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(text);
            }
        } else System.out.println(text);
    }

    public String findAllTales(File[] inputFiles) throws IOException {
        Scanner inputText = new Scanner(System.in);
        StringBuilder tail = new StringBuilder();
        int i = 1;

        if (inputFiles != null) {
            for (File inputFile : inputFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                    tail.append(inputFile).append(NEW_LINE);
                    StringBuilder text = new StringBuilder();

                    if (inputFile.length() == 0 && i != inputFiles.length) text.append(NEW_LINE);
                    else {
                        String str;
                        while (!Objects.equals(str = reader.readLine(), lastString(inputFile)))
                            text.append(str).append(NEW_LINE);
                        text.append(str);

                        tail.append(this.findTail(text));
                        if (i != inputFiles.length) tail.append(NEW_LINE);
                    }
                    i++;
                }

            }
        } else {
            StringBuilder text = new StringBuilder();
            while (inputText.hasNextLine()) {
                text.append(inputText.nextLine());
                text.append(NEW_LINE);
            }
            tail.append(this.findTail(text));
        }
        return tail.toString();
    }

    private String findTail(StringBuilder text) {
        if (charactersNumber == 0 && linesNumber == 0) return "";

        String[] lines = text.toString().split(NEW_LINE);

        if (charactersNumber != 0 && text.length() > charactersNumber)
            text.delete(0, text.length() - charactersNumber);
        else if (lines.length > linesNumber) {
            text = new StringBuilder();
            for (int i = lines.length - linesNumber; i <= lines.length - 1; i++) {
                text.append(lines[i]);
                if (i != lines.length - 1) text.append(NEW_LINE);
            }
        }

        return text.toString();
    }

    private static String lastString(File file) throws IOException {
        ReversedLinesFileReader fr = new ReversedLinesFileReader(file);
        return fr.readLine();
    }
}
