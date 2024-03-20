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

    @GetMapping("/document")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/document/each")
    public Document getDocumentById(@RequestHeader int id) {
        return documentService.getDocumentById(id);
    }
}
