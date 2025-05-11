package org.example.teachmeskills_c32_hw_finalproject.controller;

import org.example.teachmeskills_c32_hw_finalproject.service.BookFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books/{bookId}/file")
public class BookFileController {

    private final BookFileService bookFileService;

    @Autowired
    public BookFileController(BookFileService bookFileService) {
        this.bookFileService = bookFileService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file
    ) {
        boolean result = bookFileService.uploadFile(bookId, file);
        return new ResponseEntity<>(result ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping
    public ResponseEntity<List<String>> getFiles(@PathVariable Long bookId) {
        try {
            List<String> files = bookFileService.getListOfFiles(bookId);
            if (files.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long bookId,
            @PathVariable String fileName
    ) {
        Optional<Resource> file = bookFileService.getFile(bookId, fileName);
        return file.map(resource -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<HttpStatus> deleteFile(
            @PathVariable Long bookId,
            @PathVariable String fileName
    ) {
        boolean result = bookFileService.deleteFile(bookId, fileName);
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}

