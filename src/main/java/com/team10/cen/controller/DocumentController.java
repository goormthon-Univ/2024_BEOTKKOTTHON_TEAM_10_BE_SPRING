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
    public ResponseEntity<Map<String, List<Document>>> getAllDocuments(@RequestHeader(value = "Initial-Consonant", required = false) Integer initialConsonant) {
        Map<String, List<Document>> groupedDocuments = new HashMap<>();

        if (initialConsonant != null && initialConsonant >= 1 && initialConsonant <= 14) {
            String consonantKey = String.valueOf(documentService.getConsonantForNumber(initialConsonant));
            groupedDocuments.put(consonantKey, documentService.getDocumentsByInitialConsonant(initialConsonant));
        } else {
            // If Initial-Consonant header is not provided or out of range, return all documents grouped by initial consonants
            for (int i = 1; i <= 14; i++) {
                String consonantKey = String.valueOf(documentService.getConsonantForNumber(i));
                groupedDocuments.put(consonantKey, documentService.getDocumentsByInitialConsonant(i));
            }
        }

        return ResponseEntity.ok(groupedDocuments);
    }

    @GetMapping("/document/each")
    public Document getDocumentById(@RequestHeader int id) {
        return documentService.getDocumentById(id);
    }
}
