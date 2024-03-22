package com.team10.cen.service;

import com.team10.cen.domain.Document;
import com.team10.cen.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Map<String, List<Document>> getAllDocumentsGroupedByConsonant() {
        List<Document> documents = documentRepository.findAll();

        // Group documents by their initial consonants
        Map<String, List<Document>> groupedDocuments = new HashMap<>();
        for (Document document : documents) {
            String initialConsonant = getInitialConsonant(document.getTitle());
            if (!groupedDocuments.containsKey(initialConsonant)) {
                groupedDocuments.put(initialConsonant, new ArrayList<>());
            }
            groupedDocuments.get(initialConsonant).add(document);
        }

        // Sort documents within each group alphabetically by title
        groupedDocuments.forEach((key, value) ->
                value.sort((d1, d2) -> d1.getTitle().compareToIgnoreCase(d2.getTitle())));

        return groupedDocuments;
    }

    private String getInitialConsonant(String title) {
        char firstChar = title.charAt(0);
        // Korean Hangul Unicode block ranges
        if (firstChar >= 0xAC00 && firstChar <= 0xD7A3) {
            int initialValue = (firstChar - 0xAC00) / (21 * 28);
            // Map each initial consonant to its Unicode range
            String[] consonants = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
            return consonants[initialValue];
        } else {
            // For non-Hangul characters, return the character itself
            return String.valueOf(firstChar);
        }
    }


    public Document getDocumentById(int id) {
        return documentRepository.findById(id).orElse(null);
    }
}