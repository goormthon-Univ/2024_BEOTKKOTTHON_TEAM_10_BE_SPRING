package com.team10.cen.controller;

import com.team10.cen.domain.Document;
import com.team10.cen.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public List<Document> getAllDocuments(@RequestHeader(value = "Initial-Consonant", required = false) Integer initialConsonant) {
        if (initialConsonant != null && initialConsonant >= 1 && initialConsonant <= 14) {
            return documentService.getDocumentsByInitialConsonant(initialConsonant);
        } else {
            // If Initial-Consonant header is not provided or out of range, return all documents
            return documentService.getAllDocuments();
        }
    }

    @GetMapping("/document/each")
    public Document getDocumentById(@RequestHeader int id) {
        return documentService.getDocumentById(id);
    }
}
