package test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String source = "source";
        String target = "target";

        List<Path> filePaths = filePathsList(source); // Step 1: get all files from a directory
        List<Path> filteredFilePaths = filter(filePaths); // Step 2: filter by ".txt"
        List<String> contentOfFiles = getContentOfFiles(filteredFilePaths); // Step 3: get content of files
        move(filteredFilePaths, target); // Step 4: move files to destination
    }

    public static List<Path> filePathsList(String directory) throws IOException {
        List<Path> filePaths = new ArrayList<>();
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(FileSystems.getDefault().getPath(directory));
        for (Path path : directoryStream) {
            filePaths.add(path);
        }
        return filePaths;
    }

    private static List<Path> filter(List<Path> filePaths) {
        List<Path> filteredFilePaths = new ArrayList<>();
        for (Path filePath : filePaths) {
            if (filePath.getFileName().toString().endsWith(".txt")) {
                filteredFilePaths.add(filePath);
            }
        }
        return filteredFilePaths;
    }

    private static List<String> getContentOfFiles(List<Path> filePaths) throws IOException {
        List<String> contentOfFiles = new ArrayList<>(filePaths.size());
        for (Path filePath : filePaths) {
            String content = new String(Files.readAllBytes(filePath));
            contentOfFiles.add(content);
        }
        return contentOfFiles;
    }

    private static void move(List<Path> filePaths, String target) throws IOException {
        Path targetDir = FileSystems.getDefault().getPath(target);
        if (!Files.isDirectory(targetDir)) {
            targetDir = Files.createDirectories(Paths.get(target));
        }
        for (Path filePath : filePaths) {
            System.out.println("Moving " + filePath.getFileName() + " to " + targetDir.toAbsolutePath());
            Files.move(filePath, Paths.get(target, filePath.getFileName().toString()), StandardCopyOption.ATOMIC_MOVE);
        }   
    }
}