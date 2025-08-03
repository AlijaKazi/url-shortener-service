
package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    // Custom method to find a UrlMapping by its shortCode
    Optional<UrlMapping> findByShortCode(String shortCode);
}


