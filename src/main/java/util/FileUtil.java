package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static void writeToFile(String[] data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < data.length; i++) {
                writer.write(data[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static String[] formatRegisters(String[] values) {
        String[] data = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            data[i] = "R" + i + ": " + values[i];
        }

        return data;
    }
}
