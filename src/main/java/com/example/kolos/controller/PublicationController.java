package com.example.kolos.controller;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User; // Import User model to access getNickname() or getIdUser()
import com.example.kolos.service.PublicationsService;
import com.example.kolos.service.UserService; // Import UserService to find user by nickname

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import for @PreAuthorize
import org.springframework.security.core.Authentication; // Import for Authentication object
import org.springframework.security.core.context.SecurityContextHolder; // Import for SecurityContextHolder
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {
    private final PublicationsService publicationsService;
    private final UserService userService; // Inject UserService to get current user's ID

    public PublicationController(PublicationsService publicationsService, UserService userService) {
        this.publicationsService = publicationsService;
        this.userService = userService;
    }

    // --- CRUD Endpoints ---

    /**
     * Creates a new publication.
     * Only authenticated users can create publications (e.g., ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN).
     * The author of the publication should be the currently authenticated user.
     * POST /publications
     * @param publication The publication object to create.
     * @return ResponseEntity with the created Publication and HTTP status 201 (Created), or 400 (Bad Request).
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Only authenticated users (any role) can create
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        try {
            // Get the currently authenticated username (which is the nickname)
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            // Find the user by nickname to get their ID
            User currentUser = userService.findByNicknameExact(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

            // Set the author of the publication to the current user
            publication.setAuthor(currentUser); // Assuming Publication has a setAuthor(User author) method

            Publication savedPublication = publicationsService.save(publication);
            return ResponseEntity.created(URI.create("/publications/" + savedPublication.getIdPublication())).body(savedPublication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Retrieves all publications, ordered by creation date (descending).
     * Anyone (including unauthenticated users) can view all publications.
     * GET /publications
     * @return ResponseEntity with a list of publications and HTTP status 200 (OK), or 204 (No Content).
     */
    @GetMapping
    @PreAuthorize("permitAll()") // No authentication required to view all publications
    public ResponseEntity<List<Publication>> getAllPublications() {
        List<Publication> publications = publicationsService.findAllPublicationsOrderedByDate();
        return publications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(publications);
    }

    /**
     * Retrieves a publication by its ID.
     * Anyone (including unauthenticated users) can view a specific publication.
     * GET /publications/{id}
     * @param id The ID of the publication.
     * @return ResponseEntity with the Publication and HTTP status 200 (OK), or 404 (Not Found).
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()") // No authentication required to view a single publication
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id){
        return publicationsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing publication.
     * Only ADMIN or MODERATOR can update any publication.
     * A regular USER can only update their own publications.
     * PUT /publications/{id}
     * @param id The ID of the publication to update.
     * @param publicationDetails The updated publication data.
     * @return ResponseEntity with the updated Publication and HTTP status 200 (OK), or 400 (Bad Request), or 404 (Not Found), or 403 (Forbidden).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @publicationOwnershipService.isOwner(#id, authentication.name))")
    // Note: @publicationOwnershipService is a custom bean you'd need to create for ownership check
    public ResponseEntity<Publication> updatePublication(@PathVariable Long id, @RequestBody Publication publicationDetails) {
        try {
            // Ensure the author field in publicationDetails is not accidentally changed by the user
            // We should retrieve the existing publication and ensure the author remains the same,
            // or is set correctly in the service layer if allowed for ADMIN/MODERATOR.
            // For a regular user, the service should enforce that only their own publications are updated.

            Publication updated = publicationsService.update(id, publicationDetails);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Deletes a publication by its ID.
     * Only ADMIN or MODERATOR can delete any publication.
     * A regular USER can only delete their own publications.
     * DELETE /publications/{id}
     * @param id The ID of the publication to delete.
     * @return ResponseEntity with HTTP status 204 (No Content), or 404 (Not Found), or 403 (Forbidden).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @publicationOwnershipService.isOwner(#id, authentication.name))")
    // Again, @publicationOwnershipService is a custom bean for ownership check
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        try {
            publicationsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Search Endpoints ---

    /**
     * Finds publications by partial description.
     * Anyone can perform this search.
     * GET /publications/by-description?description=keyword
     * @param description Keyword to search for in descriptions.
     * @return ResponseEntity with a list of publications, or 204 (No Content).
     */
    @GetMapping("/by-description")
    @PreAuthorize("permitAll()")
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
     * Anyone can perform this search.
     * GET /publications/by-title?title=keyword
     * @param title Keyword to search for in titles.
     * @return ResponseEntity with a list of publications, or 204 (No Content).
     */
    @GetMapping("/by-title")
    @PreAuthorize("permitAll()")
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
     * Anyone can view these.
     * GET /publications/latest
     * @return ResponseEntity with a list of the 10 latest publications, or 204 (No Content).
     */
    @GetMapping("/latest")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Publication>> get10LatestPublications() {
        List<Publication> latestPublications = publicationsService.find10LatestPublication();
        return latestPublications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(latestPublications);
    }

    /**
     * Finds publications by author ID.
     * Anyone can perform this search.
     * GET /publications/by-author-id?authorId=123
     * @param authorId The ID of the author.
     * @return ResponseEntity with a list of publications, or 204 (No Content).
     */
    @GetMapping("/by-author-id")
    @PreAuthorize("permitAll()")
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