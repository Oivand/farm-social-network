package com.example.kolos.service.impl;

import com.example.kolos.model.Complaints;
import com.example.kolos.model.KindsComplaint;
import com.example.kolos.model.User;
import com.example.kolos.repository.ComplaintsRepository;
import com.example.kolos.repository.KindsComplaintRepository;
import com.example.kolos.repository.UserRepository;
import com.example.kolos.service.ComplaintsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintsServiceImpl implements ComplaintsService {

    private final ComplaintsRepository complaintsRepository;
    private final UserRepository userRepository;
    private final KindsComplaintRepository kindsComplaintRepository;

    public ComplaintsServiceImpl(ComplaintsRepository complaintsRepository,
                                 UserRepository userRepository,
                                 KindsComplaintRepository kindsComplaintRepository) {
        this.complaintsRepository = complaintsRepository;
        this.userRepository = userRepository;
        this.kindsComplaintRepository = kindsComplaintRepository;
    }

    @Override
    public List<Complaints> findComplaintsByDescriptionContaining(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Описание жалобы не может быть пустым.");
        }
        return complaintsRepository.findByDescriptionComplaintContaining(description);
    }

    @Override
    public List<Complaints> findComplaintsByAccusedId(Long accusedId) {
        if (accusedId == null) {
            throw new IllegalArgumentException("ID обвиняемого не может быть null.");
        }
        User accused = userRepository.findById(accusedId)
                .orElseThrow(() -> new IllegalArgumentException("Обвиняемый с ID " + accusedId + " не найден."));
        return complaintsRepository.findByIdAccused(accused);
    }

    @Override
    public List<Complaints> findComplaintsByAccuserId(Long accuserId) {
        if (accuserId == null) {
            throw new IllegalArgumentException("ID истца не может быть null.");
        }
        User accuser = userRepository.findById(accuserId)
                .orElseThrow(() -> new IllegalArgumentException("Истец с ID " + accuserId + " не найден."));
        return complaintsRepository.findByIdAccuser(accuser);
    }

    @Override
    public List<Complaints> findTop10ComplaintsOrderByCreatedAtDesc() {
        return complaintsRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Complaints> findComplaintsByKindId(Long kindComplaintId) {
        if (kindComplaintId == null) {
            throw new IllegalArgumentException("ID типа жалобы не может быть null.");
        }
        KindsComplaint kindComplaint = kindsComplaintRepository.findById(kindComplaintId)
                .orElseThrow(() -> new IllegalArgumentException("Тип жалобы с ID " + kindComplaintId + " не найден."));
        return complaintsRepository.findByKindComplaint(kindComplaint);
    }

    @Override
    public Optional<Complaints> findById(Long complaintId) {
        if (complaintId == null) {
            throw new IllegalArgumentException("ID жалобы не может быть null.");
        }
        return complaintsRepository.findById(complaintId);
    }

    @Override
    public List<Complaints> findAll() {
        return complaintsRepository.findAll();
    }

    @Override
    public Complaints save(Complaints complaints) {
        if (complaints == null) {
            throw new IllegalArgumentException("Объект жалобы не может быть null.");
        }
        if (complaints.getDescriptionComplaint() == null || complaints.getDescriptionComplaint().trim().isEmpty()) {
            throw new IllegalArgumentException("Описание жалобы не может быть пустым.");
        }
        if (complaints.getIdComplaint() != null) {
            throw new IllegalArgumentException("ID жалобы должно быть null для новой жалобы.");
        }

        // Валидация и привязка связанных сущностей (User и KindsComplaint)
        if (complaints.getIdAccused() == null || complaints.getIdAccused().getIdUser() == null) {
            throw new IllegalArgumentException("Обвиняемый должен быть указан (ID).");
        }
        User accusedUser = userRepository.findById(complaints.getIdAccused().getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Обвиняемый с ID " + complaints.getIdAccused().getIdUser() + " не найден."));
        complaints.setIdAccused(accusedUser);

        if (complaints.getIdAccuser() == null || complaints.getIdAccuser().getIdUser() == null) {
            throw new IllegalArgumentException("Истец должен быть указан (ID).");
        }
        User accuserUser = userRepository.findById(complaints.getIdAccuser().getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Истец с ID " + complaints.getIdAccuser().getIdUser() + " не найден."));
        complaints.setIdAccuser(accuserUser);

        if (complaints.getKindComplaint() == null || complaints.getKindComplaint().getIdKindComplaint() == null) {
            throw new IllegalArgumentException("Тип жалобы должен быть указан (ID).");
        }
        KindsComplaint kindOfComplaint = kindsComplaintRepository.findById(complaints.getKindComplaint().getIdKindComplaint())
                .orElseThrow(() -> new IllegalArgumentException("Тип жалобы с ID " + complaints.getKindComplaint().getIdKindComplaint() + " не найден."));
        complaints.setKindComplaint(kindOfComplaint);

        return complaintsRepository.save(complaints);
    }

    @Override
    public Complaints update(Long complaintId, Complaints updatedComplaints) {
        if (complaintId == null) {
            throw new IllegalArgumentException("ID жалобы для обновления не может быть null.");
        }
        if (updatedComplaints == null) {
            throw new IllegalArgumentException("Объект жалобы для обновления не может быть null.");
        }
        if (updatedComplaints.getDescriptionComplaint() == null || updatedComplaints.getDescriptionComplaint().trim().isEmpty()) {
            throw new IllegalArgumentException("Описание жалобы не может быть пустым при обновлении.");
        }

        Complaints existingComplaint = complaintsRepository.findById(complaintId)
                .orElseThrow(() -> new IllegalArgumentException("Жалоба с ID " + complaintId + " не найдена для обновления."));

        existingComplaint.setDescriptionComplaint(updatedComplaints.getDescriptionComplaint());

        if (updatedComplaints.getIdAccused() != null && updatedComplaints.getIdAccused().getIdUser() != null) {
            User newAccused = userRepository.findById(updatedComplaints.getIdAccused().getIdUser())
                    .orElseThrow(() -> new IllegalArgumentException("Новый обвиняемый с ID " + updatedComplaints.getIdAccused().getIdUser() + " не найден."));
            existingComplaint.setIdAccused(newAccused);
        } else if (updatedComplaints.getIdAccused() != null) {
            existingComplaint.setIdAccused(null);
        }

        if (updatedComplaints.getIdAccuser() != null && updatedComplaints.getIdAccuser().getIdUser() != null) {
            User newAccuser = userRepository.findById(updatedComplaints.getIdAccuser().getIdUser())
                    .orElseThrow(() -> new IllegalArgumentException("Новый истец с ID " + updatedComplaints.getIdAccuser().getIdUser() + " не найден."));
            existingComplaint.setIdAccuser(newAccuser);
        } else if (updatedComplaints.getIdAccuser() != null) {
            existingComplaint.setIdAccuser(null);
        }

        if (updatedComplaints.getKindComplaint() != null && updatedComplaints.getKindComplaint().getIdKindComplaint() != null) {
            KindsComplaint newKind = kindsComplaintRepository.findById(updatedComplaints.getKindComplaint().getIdKindComplaint())
                    .orElseThrow(() -> new IllegalArgumentException("Новый тип жалобы с ID " + updatedComplaints.getKindComplaint().getIdKindComplaint() + " не найден."));
            existingComplaint.setKindComplaint(newKind);
        } else if (updatedComplaints.getKindComplaint() != null) {
            existingComplaint.setKindComplaint(null);
        }

        return complaintsRepository.save(existingComplaint);
    }

    @Override
    public void delete(Long complaintId) {
        if (complaintId == null) {
            throw new IllegalArgumentException("ID жалобы для удаления не может быть null.");
        }
        if (!complaintsRepository.existsById(complaintId)) {
            throw new IllegalArgumentException("Жалоба с ID " + complaintId + " не найдена для удаления.");
        }
        complaintsRepository.deleteById(complaintId);
    }
}