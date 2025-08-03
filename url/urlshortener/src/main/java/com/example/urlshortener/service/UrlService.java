package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    // Characters allowed in the short code
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // Length of the generated short code
    private static final int SHORT_CODE_LENGTH = 6;
    private static Random random = new Random();

    /**
     * Generates a random short code.
     * @return A randomly generated short code string.
     */
    private String generateShortCode() {
        StringBuilder shortCodeBuilder = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            shortCodeBuilder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shortCodeBuilder.toString();
    }

    /**
     * Creates a new short URL for a given long URL.
     * Generates a unique short code and saves the mapping to the database.
     * @param longUrl The original long URL to shorten.
     * @return The UrlMapping object containing the long URL and the generated short code.
     */
    public UrlMapping createShortUrl(String longUrl) {
        // Generate a unique short code. Loop until a unique one is found.
        String shortCode;
        Optional<UrlMapping> existingMapping;
        do {
            shortCode = generateShortCode();
            existingMapping = urlRepository.findByShortCode(shortCode);
        } while (existingMapping.isPresent()); // Keep generating if the code already exists

        UrlMapping urlMapping = new UrlMapping(longUrl, shortCode);
        return urlRepository.save(urlMapping);
    }

    /**
     * Retrieves the original long URL for a given short code and increments its access count.
     * @param shortCode The short code to look up.
     * @return An Optional containing the UrlMapping if found, otherwise empty.
     */
    public Optional<UrlMapping> getOriginalUrl(String shortCode) {
        Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();
            // Increment access count
            mapping.setAccessCount(mapping.getAccessCount() + 1);
            urlRepository.save(mapping); // Save the updated count
        }
        return urlMapping;
    }

    /**
     * Retrieves statistics for a given short code without incrementing access count.
     * @param shortCode The short code to retrieve stats for.
     * @return An Optional containing the UrlMapping if found, otherwise empty.
     */
    public Optional<UrlMapping> getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }
}

