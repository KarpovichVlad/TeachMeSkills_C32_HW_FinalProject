package org.example.teachmeskills_c32_hw_finalproject.service;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookFileService {

    private final Path ROOT_FILE_PATH = Paths.get("data");

    private Path getBookPath(Long bookId) {
        return ROOT_FILE_PATH.resolve(bookId.toString());
    }

    public Boolean uploadFile(Long bookId, MultipartFile file) {
        try {
            if (file.getOriginalFilename() == null) {
                return false;
            }
            Path bookPath = getBookPath(bookId);
            Files.createDirectories(bookPath); // Создаём папку для книги, если её нет
            Files.copy(file.getInputStream(), bookPath.resolve(file.getOriginalFilename()));
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    public Optional<Resource> getFile(Long bookId, String fileName) {
        Path path = getBookPath(bookId).resolve(fileName);
        Resource resource = new PathResource(path);
        if (resource.exists()) {
            return Optional.of(resource);
        }
        return Optional.empty();
    }

    public ArrayList<String> getListOfFiles(Long bookId) throws IOException {
        Path path = getBookPath(bookId);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return (ArrayList<String>) Files
                .walk(path, 1)
                .filter(p -> !p.equals(path))
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }

    public Boolean deleteFile(Long bookId, String fileName) {
        Path path = getBookPath(bookId).resolve(fileName);
        File file = new File(path.toString());
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}