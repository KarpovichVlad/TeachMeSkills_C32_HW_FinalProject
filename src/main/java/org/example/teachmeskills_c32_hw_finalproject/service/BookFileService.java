package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookFileService {

    private final Path ROOT_FILE_PATH = Paths.get("data");
    private final BookRepository bookRepository;

    @Autowired
    public BookFileService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private Path getBookPath(Long bookId) {
        return ROOT_FILE_PATH.resolve(bookId.toString());
    }

    private boolean bookExists(Long bookId) {
        return bookRepository.existsById(bookId);
    }

    public boolean uploadFile(Long bookId, MultipartFile file) {
        if (!bookExists(bookId) || file.getOriginalFilename() == null)
            return false;

        try {
            Path bookPath = getBookPath(bookId);
            Files.createDirectories(bookPath);

            Path destination = bookPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Optional<Resource> getFile(Long bookId, String fileName) {
        if (!bookExists(bookId)) return Optional.empty();

        Path path = getBookPath(bookId).resolve(fileName);
        Resource resource = new PathResource(path);
        return resource.exists() ? Optional.of(resource) : Optional.empty();
    }

    public List<String> getListOfFiles(Long bookId) throws IOException {
        if (!bookExists(bookId)) return new ArrayList<>();

        Path path = getBookPath(bookId);
        if (!Files.exists(path)) return new ArrayList<>();

        try (var stream = Files.list(path)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public boolean deleteFile(Long bookId, String fileName) {
        if (!bookExists(bookId)) return false;

        Path path = getBookPath(bookId).resolve(fileName);
        File file = path.toFile();
        return file.exists() && file.delete();
    }
}
