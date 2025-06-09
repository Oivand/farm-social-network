package com.example.kolos.controller;

import com.example.kolos.model.Complaints;
import com.example.kolos.service.ComplaintsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintsController {
    private final ComplaintsService complaintsService;

    public ComplaintsController(ComplaintsService complaintsService) {
        this.complaintsService = complaintsService;
    }

    @GetMapping
    public ResponseEntity<List<Complaints>> getAll(){
        List<Complaints> complaintsList = complaintsService.findAll();
        return complaintsList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaints> getById(@PathVariable Long id){
        return complaintsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-description")
    public ResponseEntity<List<Complaints>> getComplaintsByDescription(@RequestParam String description) {
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaints = complaintsService.findComplaintsByDescriptionContaining(description);
        return complaints.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(complaints);
    }

    @GetMapping("/by-accused")
    public ResponseEntity<List<Complaints>> getComplaintsByAccused(@RequestParam Long accusedId){
        if(accusedId == null){
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaintsListByAccused = complaintsService.findComplaintsByAccusedId(accusedId);
        return complaintsListByAccused.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsListByAccused);
    }

    @GetMapping("/by-accuser")
    public ResponseEntity<List<Complaints>> getComplaintsByAccuser(@RequestParam Long accuserId){
        if(accuserId == null){
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaintsListAccuser = complaintsService.findComplaintsByAccuserId(accuserId);
        return complaintsListAccuser.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsListAccuser);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Complaints>> getTop10LatestComplaints(){
        List<Complaints> complaintsLatest = complaintsService.findTop10ComplaintsOrderByCreatedAtDesc();
        return complaintsLatest.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsLatest);
    }

    @GetMapping("/by-kind")
    public ResponseEntity<List<Complaints>> getComplaintsByKind(@RequestParam Long kindId){
        if(kindId == null){
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaintsListKind = complaintsService.findComplaintsByKindId(kindId);
        return complaintsListKind.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsListKind);
    }

    @PostMapping
    public ResponseEntity<Complaints> createComplaint(@RequestBody Complaints complaint) {
        if (complaint == null || complaint.getDescriptionComplaint() == null || complaint.getDescriptionComplaint().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (complaint.getIdComplaint() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        Complaints savedComplaint = complaintsService.save(complaint);
        return ResponseEntity.created(URI.create("/complaints/" + savedComplaint.getIdComplaint())).body(savedComplaint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Complaints> updateComplaint(@PathVariable Long id, @RequestBody Complaints complaint) {
        if (complaint == null || complaint.getDescriptionComplaint() == null || complaint.getDescriptionComplaint().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (complaint.getIdComplaint() == null || !complaint.getIdComplaint().equals(id)) {
            complaint.setIdComplaint(id);
        }
        Complaints updatedComplaint = complaintsService.update(id, complaint);
        return ResponseEntity.ok(updatedComplaint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}