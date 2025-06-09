package com.example.kolos.controller;

import com.example.kolos.model.KindsComplaint;
import com.example.kolos.service.KindsComplaintService;
import org.springframework.http.HttpStatus; // For HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI; // For Location header

import java.util.List;


@RestController
@RequestMapping("/kindsComplaint")
public class KindsComplaintController {
    private final KindsComplaintService kindsComplaintService;

    public KindsComplaintController(KindsComplaintService kindsComplaintService) {
        this.kindsComplaintService = kindsComplaintService;
    }

    @GetMapping
    public List<KindsComplaint> getAllKindComplaints(){
        return kindsComplaintService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KindsComplaint> getKindsComplaintById(@PathVariable Long id){
        return kindsComplaintService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    // Now performs an exact search, returning a list (0 or 1 element)
    public ResponseEntity<List<KindsComplaint>> getKindComplaintByName(@RequestParam String name) { // Renamed param to 'name' for clarity
        if(name == null || name.trim().isEmpty()){ // Added trim()
            return ResponseEntity.badRequest().build();
        }
        List<KindsComplaint> kinds = kindsComplaintService.findKindByName(name); // Use the renamed service method
        // If list is empty, return 204 No Content. Otherwise, 200 OK with the list.
        return kinds.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(kinds);
    }

    @PostMapping
    // Renamed method to be more descriptive
    public ResponseEntity<KindsComplaint> createKindComplaint(@RequestBody KindsComplaint kind) {
        if (kind == null || kind.getNameKindComplaint() == null || kind.getNameKindComplaint().trim().isEmpty()) { // Added trim()
            return ResponseEntity.badRequest().build();
        }
        // For new resources, ID should be null
        if (kind.getIdKindComplaint() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Or include a more specific error message
        }
        KindsComplaint savedKind = kindsComplaintService.save(kind);
        // Return 201 Created status and include the Location header
        return ResponseEntity.created(URI.create("/kindsComplaint/" + savedKind.getIdKindComplaint())).body(savedKind);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKindsComplaint(@PathVariable Long id) {
        kindsComplaintService.delete(id);
        // Return 204 No Content for successful deletion
        return ResponseEntity.noContent().build();
    }
}