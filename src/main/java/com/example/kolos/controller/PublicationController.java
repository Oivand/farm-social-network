package com.example.kolos.controller;

import com.example.kolos.model.Publication;
import com.example.kolos.service.PublicationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {
    private final PublicationsService publicationsService;

    public PublicationController(PublicationsService publicationsService) {
        this.publicationsService = publicationsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id){
        return publicationsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-description")
    public ResponseEntity<List<Publication>> findPublicationByDescription(@RequestParam String description){
        if (description == null || description.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Publication> publicationList = publicationsService.findPublicationByDescription(description);
        return publicationList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publicationList);
    }

    @GetMapping("/by-title")
    public ResponseEntity<List<Publication>> findPublicationByTitle(@RequestParam String title) {
        if (title == null || title.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Publication> publicationList = publicationsService.findPublicationByTitle(title);
        return publicationList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publicationList);
    }

    @GetMapping("/latest-news")
    public ResponseEntity<List<Publication>> get10LatestPublications() {
        List<Publication> latestPublications = publicationsService.find10LatestPublication();
        return latestPublications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(latestPublications);
    }

    @GetMapping("/by-author")
    public ResponseEntity<List<Publication>> findPublicationsByAuthor(@RequestParam Long authorId) {
        if (authorId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Publication> publications = publicationsService.findPublicationByAuthorId(authorId);
        return publications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publications);
    }


}
