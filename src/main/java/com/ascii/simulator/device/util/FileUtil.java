package com.ascii.simulator.device.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

import static com.ascii.simulator.device.util.Constants.TRAINING_DATA_FILE_FORMAT_REGEX_SPLIT_ESCAPE_NEWLINE;

public class FileUtil {
    public static void readDataRecursively(Path path, List<Path> deviceFiles) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                deviceFiles.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void saveDataToFile(String path, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(data);
        writer.close();
    }

    public static String readFileData(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);
        return lines.stream().collect(Collectors.joining(TRAINING_DATA_FILE_FORMAT_REGEX_SPLIT_ESCAPE_NEWLINE));
    }
}
