package com.example.kolos.controller;

import com.example.kolos.model.Complaints;
import com.example.kolos.service.ComplaintsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return complaintsService.findById(id).
                map(ResponseEntity::ok).
                orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-accused")
    public ResponseEntity<List<Complaints>> getComplaintsByAccused(@RequestParam Long idAccused){
        if(idAccused == null){
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaintsListByAccused = complaintsService.findComplaintsByAccusedId(idAccused);
        return complaintsListByAccused.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsListByAccused);
    }

    @GetMapping("/by-accuser")
    public ResponseEntity<List<Complaints>> getComplaintsByAccuser(@RequestParam Long idAccuser){
        if(idAccuser == null){
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaintsListAccuser = complaintsService.findComplaintsByAccuser(idAccuser);
        return complaintsListAccuser.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsListAccuser);
    }

    @GetMapping("/latest-complaints")
    public ResponseEntity<List<Complaints>> getComplaintsLatest(){
        List<Complaints> complaintsLatest = complaintsService.findComplaints10Latest();
        return complaintsLatest.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsLatest);
    }

    @GetMapping("/latest-complaints-by-kind")
    public ResponseEntity<List<Complaints>> getComplaintsLatestByKind(@RequestParam Long idKind){
        if(idKind == null){
            return ResponseEntity.badRequest().build();
        }
        List<Complaints> complaintsListKind = complaintsService.findComplaintsByKind(idKind);
        return complaintsListKind.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(complaintsListKind);
    }


}
