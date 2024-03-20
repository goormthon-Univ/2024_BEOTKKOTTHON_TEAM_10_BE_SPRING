package com.team10.cen.service;

import com.team10.cen.domain.Document;
import com.team10.cen.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> getAllDocuments() {
        // Fetch all documents from the repository and sort them alphabetically by title
        List<Document> documents = documentRepository.findAll();
        return documents.stream()
                .sorted((d1, d2) -> d1.getTitle().compareToIgnoreCase(d2.getTitle()))
                .collect(Collectors.toList());
    }

    public Document getDocumentById(int id) {
        return documentRepository.findById(id).orElse(null);
    }
}