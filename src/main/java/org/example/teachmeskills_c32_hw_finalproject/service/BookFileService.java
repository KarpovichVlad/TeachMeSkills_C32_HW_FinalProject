package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.BookNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.FileNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(BookFileService.class);

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
        if (!bookExists(bookId)) {
            logger.error("Книга с ID {} не найдена", bookId);
            throw new BookNotFoundException(bookId);
        }
        if (file.getOriginalFilename() == null) {
            logger.error("Не указано имя файла при загрузке");
            return false;
        }

        try {
            Path bookPath = getBookPath(bookId);
            Files.createDirectories(bookPath);

            Path destination = bookPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            logger.error("Ошибка при загрузке файла для книги ID {}: {}", bookId, e.getMessage(), e);
            return false;
        }
    }

    public Optional<Resource> getFile(Long bookId, String fileName) {
        if (!bookExists(bookId)) {
            logger.error("Книга с ID {} не найдена", bookId);
            throw new BookNotFoundException(bookId);
        }

        Path path = getBookPath(bookId).resolve(fileName);
        Resource resource = new PathResource(path);
        if (!resource.exists()) {
            logger.error("Файл '{}' не найден для книги с ID {}", fileName, bookId);
            throw new FileNotFoundException(fileName);
        }
        return Optional.of(resource);
    }

    public List<String> getListOfFiles(Long bookId) throws IOException {
        if (!bookExists(bookId)) {
            logger.error("Книга с ID {} не найдена", bookId);
            throw new BookNotFoundException(bookId);
        }

        Path path = getBookPath(bookId);
        if (!Files.exists(path)) return new ArrayList<>();

        try (var stream = Files.list(path)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Ошибка при чтении файлов книги ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }

    public boolean deleteFile(Long bookId, String fileName) {
        if (!bookExists(bookId)) {
            logger.error("Книга с ID {} не найдена", bookId);
            throw new BookNotFoundException(bookId);
        }
        Path path = getBookPath(bookId).resolve(fileName);
        File file = path.toFile();
        if (!file.exists()) {
            logger.error("Файл '{}' не найден для удаления (книга ID {})", fileName, bookId);
            throw new FileNotFoundException(fileName);
        }
        boolean deleted = file.delete();
        if (deleted) {
            File folder = getBookPath(bookId).toFile();
            String[] remainingFiles = folder.list((dir, name) -> new File(dir, name).isFile());
            if (remainingFiles != null && remainingFiles.length == 0) {
                boolean folderDeleted = folder.delete();
                if (folderDeleted) {
                    logger.info("Папка книги с ID {} удалена, так как в ней не осталось файлов", bookId);
                } else {
                    logger.warn("Не удалось удалить папку книги с ID {}, хотя она пуста", bookId);
                }
            }
        }
        return deleted;
    }
}