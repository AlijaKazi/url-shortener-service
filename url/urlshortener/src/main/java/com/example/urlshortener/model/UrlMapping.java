package com.example.urlshortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "url_mapping")
@Data // Lombok annotation for getters, setters, toString, etc.
@NoArgsConstructor // Lombok annotation for a no-args constructor
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String longUrl;
    private String shortCode;
    private LocalDateTime creationDate;
    private Long accessCount = 0L;

    public UrlMapping(String longUrl, String shortCode) {
        this.longUrl = longUrl;
        this.shortCode = shortCode;
        this.creationDate = LocalDateTime.now();
    }
}


