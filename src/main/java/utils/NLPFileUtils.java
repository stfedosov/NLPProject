package utils;

import com.google.common.base.Charsets;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class NLPFileUtils {

    public static List<String> readAllLines(final String inputFile) throws IOException {
        return Files.readAllLines(Paths.get(inputFile), Charsets.UTF_8);
    }

    public static void writeAllLines(final Path outputFile,
                                     final List<String[]> data,
                                     final String[] headers) throws IOException {
        final CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile.toString()));
        csvWriter.writeNext(headers);
        csvWriter.writeAll(data);
        csvWriter.flushQuietly();
        csvWriter.close();
    }

    public static void prepareOutputFile(final Path outputFile) throws IOException {
        if (Files.exists(outputFile) && Files.size(outputFile) >= 0) {
            Files.delete(outputFile);
        }
        Files.createFile(outputFile);
    }
}