package com.example.kolos.repository;

import com.example.kolos.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationsRepositor extends JpaRepository<Publication, Long> {
}
