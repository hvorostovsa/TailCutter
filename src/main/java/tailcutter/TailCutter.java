package tailcutter;

import java.io.*;
import java.util.Scanner;

public class TailCutter {

    private final int charactersNumber;
    private final int linesNumber;

    public TailCutter(int charactersNumber, int linesNumber) {
        this.charactersNumber = charactersNumber;
        this.linesNumber = linesNumber;
    }

    public void cutFileTail(String[] inputFileNames, String outputFileName) {
        Scanner inputText = new Scanner(System.in);
        StringBuilder tail = new StringBuilder();

        if (inputFileNames != null) {
            for (String inputFileName : inputFileNames) {
                try {
                    tail.append(inputFileName).append("\n");
                    StringBuilder text = new StringBuilder();
                    BufferedReader inputFile = new BufferedReader(new FileReader(inputFileName));

                    String str;
                    while ((str = inputFile.readLine()) != null)
                        text.append(str).append("\n");
                    text.deleteCharAt(text.length() - 1);
                    tail.append(this.findTail(text)).append("\n");

                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
            tail.deleteCharAt(tail.length() - 1);
        } else {
            StringBuilder text = new StringBuilder().append(inputText.nextLine());
            tail.append(this.findTail(text));
        }

        if (outputFileName != null) {
            try {
                BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
                outputFile.write(tail.toString());
                outputFile.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else System.out.println(tail);
    }

    private String findTail(StringBuilder text) {
        if (charactersNumber == 0 && linesNumber == 0) return "";

        text.reverse();
        String[] lines = text.toString().split("\n");
            if (charactersNumber != 0 && text.length() > charactersNumber)
                text.delete(charactersNumber, text.length());
            else if (lines.length > linesNumber) {
                    text = new StringBuilder();
                    for (int line = 0; line <= linesNumber - 1; line++) {
                        text.append(lines[line]).append("\n");
                    }
                    text.deleteCharAt(text.length() - 1);
            }
        text.reverse();
        return text.toString();
    }
}
