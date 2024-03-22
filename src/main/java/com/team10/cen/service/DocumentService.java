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

    public List<Document> getDocumentsByInitialConsonant(int initialConsonant) {
        // Fetch all documents from the repository
        List<Document> documents = documentRepository.findAll();

        // Filter documents based on initial consonant
        List<Document> filteredDocuments = documents.stream()
                .filter(document -> {
                    char firstChar = document.getTitle().charAt(0);
                    char chosenConsonant = getConsonantForNumber(initialConsonant);
                    return isSameConsonant(firstChar, chosenConsonant);
                })
                .sorted((d1, d2) -> d1.getTitle().compareToIgnoreCase(d2.getTitle()))
                .collect(Collectors.toList());

        return filteredDocuments;
    }

    // Helper method to determine if the given character is the chosen consonant
    private boolean isSameConsonant(char firstChar, char chosenConsonant) {
        // Korean consonants (initial)
        char[] consonants = {'ㄱ', 'ㄴ', 'ㄷ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅅ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

        // Get the index of the consonant
        int index = (firstChar - '가') / 588;

        // Check if it matches the chosen consonant
        return consonants[index] == chosenConsonant;
    }

    // Helper method to get the consonant corresponding to the provided number
    private char getConsonantForNumber(int number) {
        // Korean consonants (initial)
        char[] consonants = {'ㄱ', 'ㄴ', 'ㄷ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅅ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
        return consonants[number - 1]; // Adjusting for 0-based index
    }

    public Document getDocumentById(int id) {
        return documentRepository.findById(id).orElse(null);
    }
}