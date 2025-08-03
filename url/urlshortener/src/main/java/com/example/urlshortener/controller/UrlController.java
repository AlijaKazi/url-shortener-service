package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    /**
     * Endpoint to create a short URL.
     * @param requestBody A JSON object containing the "longUrl".
     * @return A ResponseEntity with the generated short URL or an error message.
     */
    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody Map<String, String> requestBody) {
        String longUrl = requestBody.get("longUrl");
        if (longUrl == null || longUrl.isEmpty()) {
            // Return a bad request if longUrl is missing or empty
            return ResponseEntity.badRequest().body(Map.of("error", "longUrl must not be empty"));
        }
        try {
        new java.net.URL(longUrl); // Use full qualified name or import java.net.URL
    } catch (java.net.MalformedURLException e) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid URL format"));
    }
        UrlMapping urlMapping = urlService.createShortUrl(longUrl);
        // Construct the full short URL. Assuming the application runs on port 8080.
        String shortUrl = "http://localhost:8080/" + urlMapping.getShortCode();
        return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
    }

    /**
     * Endpoint to redirect to the original long URL using the short code.
     * @param shortCode The short code to redirect from.
     * @return A ResponseEntity with a 302 Found status and the original URL in the Location header,
     * or a 404 Not Found if the short code is invalid.
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        Optional<UrlMapping> urlMapping = urlService.getOriginalUrl(shortCode);
        if (urlMapping.isPresent()) {
            // If mapping found, set Location header and return 302 Found
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(urlMapping.get().getLongUrl()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302
        } else {
            // If not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to view statistics for a given short code.
     * @param shortCode The short code to retrieve statistics for.
     * @return A ResponseEntity with the UrlMapping object containing stats,
     * or a 404 Not Found if the short code is invalid.
     */
    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<UrlMapping> getStats(@PathVariable String shortCode) {
        return urlService.getStats(shortCode)
                .map(ResponseEntity::ok) // If found, return 200 OK with the mapping
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }
}
