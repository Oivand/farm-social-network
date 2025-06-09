package com.example.kolos.controller;

import com.example.kolos.model.Publication;
import com.example.kolos.service.PublicationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI; // Import URI for ResponseEntity.created()

import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {
    private final PublicationsService publicationsService;

    public PublicationController(PublicationsService publicationsService) {
        this.publicationsService = publicationsService;
    }

    // --- CRUD Endpoints ---

    /**
     * Creates a new publication.
     * POST /publications
     * @param publication The publication object to create.
     * @return ResponseEntity with the created Publication and HTTP status 201 (Created), or 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        try {
            Publication savedPublication = publicationsService.save(publication);
            return ResponseEntity.created(URI.create("/publications/" + savedPublication.getIdPublication())).body(savedPublication);
        } catch (IllegalArgumentException e) {
            // Handle validation errors from the service
            return ResponseEntity.badRequest().body(null); // Consider sending a more descriptive error message
        }
    }

    /**
     * Retrieves all publications, ordered by creation date (descending).
     * GET /publications
     * @return ResponseEntity with a list of publications and HTTP status 200 (OK), or 204 (No Content).
     */
    @GetMapping
    public ResponseEntity<List<Publication>> getAllPublications() {
        List<Publication> publications = publicationsService.findAllPublicationsOrderedByDate();
        return publications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publications);
    }

    /**
     * Retrieves a publication by its ID.
     * GET /publications/{id}
     * @param id The ID of the publication.
     * @return ResponseEntity with the Publication and HTTP status 200 (OK), or 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id){
        return publicationsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing publication.
     * PUT /publications/{id}
     * @param id The ID of the publication to update.
     * @param publicationDetails The updated publication data.
     * @return ResponseEntity with the updated Publication and HTTP status 200 (OK), or 400 (Bad Request), or 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(@PathVariable Long id, @RequestBody Publication publicationDetails) {
        try {
            Publication updated = publicationsService.update(id, publicationDetails);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            // Catch specific exceptions if you want different status codes (e.g., 404 for not found)
            if (e.getMessage().contains("not found")) { // Simple check, consider custom exceptions for robustness
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null); // Validation errors or other bad input
        }
    }

    /**
     * Deletes a publication by its ID.
     * DELETE /publications/{id}
     * @param id The ID of the publication to delete.
     * @return ResponseEntity with HTTP status 204 (No Content), or 404 (Not Found).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        try {
            publicationsService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
        } catch (IllegalArgumentException e) {
            // If publication not found
            return ResponseEntity.notFound().build();
        }
    }

    // --- Search Endpoints ---

    /**
     * Finds publications by partial description.
     * GET /publications/by-description?description=keyword
     * @param description Keyword to search for in descriptions.
     * @return ResponseEntity with a list of publications, or 204 (No Content).
     */
    @GetMapping("/by-description")
    public ResponseEntity<List<Publication>> findPublicationByDescription(@RequestParam String description){
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Publication> publicationList = publicationsService.findPublicationByDescription(description);
        return publicationList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publicationList);
    }

    /**
     * Finds publications by partial title.
     * GET /publications/by-title?title=keyword
     * @param title Keyword to search for in titles.
     * @return ResponseEntity with a list of publications, or 204 (No Content).
     */
    @GetMapping("/by-title")
    public ResponseEntity<List<Publication>> findPublicationByTitle(@RequestParam String title) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Publication> publicationList = publicationsService.findPublicationByTitle(title);
        return publicationList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publicationList);
    }

    /**
     * Retrieves the 10 most recent publications.
     * GET /publications/latest
     * @return ResponseEntity with a list of the 10 latest publications, or 204 (No Content).
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Publication>> get10LatestPublications() {
        List<Publication> latestPublications = publicationsService.find10LatestPublication();
        return latestPublications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(latestPublications);
    }

    /**
     * Finds publications by author ID.
     * GET /publications/by-author-id?authorId=123
     * @param authorId The ID of the author.
     * @return ResponseEntity with a list of publications, or 204 (No Content).
     */
    @GetMapping("/by-author-id") // Changed endpoint path for clarity
    public ResponseEntity<List<Publication>> findPublicationsByAuthorId(@RequestParam Long authorId) {
        if (authorId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Publication> publications = publicationsService.findPublicationByAuthorId(authorId);
        return publications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publications);
    }
}