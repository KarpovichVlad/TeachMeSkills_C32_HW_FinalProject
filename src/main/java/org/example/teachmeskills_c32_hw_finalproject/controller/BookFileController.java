package org.example.teachmeskills_c32_hw_finalproject.controller;


import org.example.teachmeskills_c32_hw_finalproject.service.BookFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public ResponseEntity<HttpStatus> uploadFile(@PathVariable Long bookId, @RequestParam("file") MultipartFile file) {
        Boolean result = bookFileService.uploadFile(bookId, file);
        return new ResponseEntity<>(result ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable Long bookId, @PathVariable String filename) {
        Optional<Resource> resource = bookFileService.getFile(bookId, filename);
        return resource.map(res -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + res.getFilename());
            return new ResponseEntity<>(res, headers, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<ArrayList<String>> getListOfFiles(@PathVariable Long bookId) {
        ArrayList<String> files;
        try {
            files = bookFileService.getListOfFiles(bookId);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (files.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable Long bookId, @PathVariable String filename) {
        Boolean result = bookFileService.deleteFile(bookId, filename);
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}

