package com.example.kolos.controller;

import com.example.kolos.model.KindsComplaint;
import com.example.kolos.service.KindsComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<KindsComplaint>> getKindsComplaintByName(@RequestParam String kind){
        if(kind == null || kind.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        List<KindsComplaint> kinds = kindsComplaintService.getKindComplaintsByName(kind);
        return kinds.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(kinds);
    }

    @PostMapping
    public ResponseEntity<KindsComplaint> addKind(@RequestBody KindsComplaint kind) {
        KindsComplaint saved = kindsComplaintService.save(kind);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKindsComplaint(@PathVariable Long id) {
        kindsComplaintService.delete(id);
        return ResponseEntity.noContent().build(); // Возвращает статус 204, что означает успешное удаление
    }
}
