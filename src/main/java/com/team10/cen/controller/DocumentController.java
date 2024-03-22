package com.team10.cen.controller;

import com.team10.cen.domain.Document;
import com.team10.cen.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @GetMapping("/documents")
    public ResponseEntity<Map<String, List<Document>>> getAllDocumentsGroupedByConsonant() {
        Map<String, List<Document>> groupedDocuments = documentService.getAllDocumentsGroupedByConsonant();
        return ResponseEntity.ok(groupedDocuments);
    }

    @GetMapping("/document/each")
    public Document getDocumentById(@RequestHeader int id) {
        return documentService.getDocumentById(id);
    }
}
