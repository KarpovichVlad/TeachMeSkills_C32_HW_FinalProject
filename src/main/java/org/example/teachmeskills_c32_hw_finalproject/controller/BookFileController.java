package org.example.teachmeskills_c32_hw_finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.BookNotFoundException;
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
@Tag(name = "Book File Controller", description = "Manage book file upload, download, and deletion")
public class BookFileController {

    private final BookFileService bookFileService;

    @Autowired
    public BookFileController(BookFileService bookFileService) {
        this.bookFileService = bookFileService;
    }

    @Operation(summary = "Upload a file for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred during file upload")
    })
    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file
    ) {
        boolean result = bookFileService.uploadFile(bookId, file);
        return new ResponseEntity<>(result ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @Operation(summary = "Get a list of uploaded files for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of files returned successfully (empty list if no files)"),
            @ApiResponse(responseCode = "404", description = "Book not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error while retrieving files")
    })
    @GetMapping("/list")
    public ResponseEntity<List<String>> getFiles(@PathVariable Long bookId) {
        try {
            List<String> files = bookFileService.getListOfFiles(bookId);
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Download a specific file for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long bookId,
            @PathVariable String fileName
    ) {
        Optional<Resource> file = bookFileService.getFile(bookId, fileName);
        return file.map(resource -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a specific file for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted successfully"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    @DeleteMapping("/{fileName}")
    public ResponseEntity<HttpStatus> deleteFile(
            @PathVariable Long bookId,
            @PathVariable String fileName
    ) {
        boolean result = bookFileService.deleteFile(bookId, fileName);
        return new ResponseEntity<>(result ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}

